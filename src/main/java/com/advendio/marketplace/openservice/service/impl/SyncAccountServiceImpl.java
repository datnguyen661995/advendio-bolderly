/* (C)2022 */
package com.advendio.marketplace.openservice.service.impl;

import com.advendio.marketplace.openservice.constants.SFConstant;
import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.advendio.marketplace.openservice.service.SForceService;
import com.advendio.marketplace.openservice.service.SyncAccountService;
import com.sforce.async.*;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.AggregateResult;
import com.sforce.soap.enterprise.sobject.SObject;
import java.io.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class SyncAccountServiceImpl implements SyncAccountService {
    @Autowired private SForceService sForceService;

    @Override
    public List<Account> getAccounts(DataRequest dataRequest, String connnectionType) {
        List<Account> accounts = getGroupPublisherFromAccounts(dataRequest, connnectionType);
        return accounts;
    }

    @Override
    public void createAccounts(
            String sobjectType, String userName, String endPoint, String connnectionType)
            throws AsyncApiException, IOException {
        OAuthConnectionDto connectionDto =
                sForceService.getAllConnection(userName, endPoint, SFConstant.BULK_CONNECTION);
        BulkConnection connection = connectionDto.getBulkConnection();
        JobInfo job = createJob(sobjectType, connectionDto.getBulkConnection());
        List<BatchInfo> batchInfoList =
                createBatchesFromCSVFile(
                        connection, job, "C:/Users/Dat Nguyen/Downloads/mySampleData.csv");
        awaitCompletion(connection, job, batchInfoList);
        checkResults(connection, job, batchInfoList);
        closeJob(connection, job.getId());
    }

    private List<Account> getGroupPublisherFromAccounts(
            DataRequest request, String connnectionType) {
        List<Account> pulishers = new ArrayList<>();
        try {
            StringBuilder soqlQueryBuilder = new StringBuilder();
            soqlQueryBuilder.append("SELECT ");
            soqlQueryBuilder.append("ADvendioMP__PublisherSfUser__c PublisherSfUser__c, ");
            soqlQueryBuilder.append("ADvendioMP__PublisherSfEndpoint__c PublisherSfEndpoint__c, ");
            soqlQueryBuilder.append("Id, ");
            soqlQueryBuilder.append("Name ");
            soqlQueryBuilder.append("FROM Account ");
            soqlQueryBuilder.append("WHERE ");
            soqlQueryBuilder.append("RecordType.DeveloperName ='Publisher' ");
            soqlQueryBuilder.append(
                    "AND ADvendioMP__PublisherSfUser__c != NULL AND ADvendioMP__PublisherSfUser__c != '' ");
            soqlQueryBuilder.append(
                    "AND ADvendioMP__PublisherSfEndpoint__c != NULL AND ADvendioMP__PublisherSfEndpoint__c != '' ");
            if (!ObjectUtils.isEmpty(request.getRecordid())) {
                soqlQueryBuilder.append(String.format("AND Id ='%s' ", request.getRecordid()));
            }
            soqlQueryBuilder.append("GROUP BY ");
            soqlQueryBuilder.append("ADvendioMP__PublisherSfUser__c, ");
            soqlQueryBuilder.append("ADvendioMP__PublisherSfEndpoint__c, ");
            soqlQueryBuilder.append("Id, ");
            soqlQueryBuilder.append("Name ");

            String soqlQuery = soqlQueryBuilder.toString();
            log.debug("sql::" + soqlQuery);

            QueryResult qr = sForceService.query(request, connnectionType, soqlQuery);
            if (qr.getSize() > 0) {
                log.debug("Query returned {} results.", qr.getRecords().length);
                for (SObject sObj : qr.getRecords()) {
                    AggregateResult result = (AggregateResult) sObj;
                    Account account = new Account();
                    account.setADvendioMP__PublisherSfUser__c(
                            result.getField("PublisherSfUser__c") + "");
                    account.setADvendioMP__PublisherSfEndpoint__c(
                            result.getField("PublisherSfEndpoint__c") + "");
                    account.setId(result.getField("Id") + "");
                    account.setName(result.getField("Name") + "");
                    pulishers.add(account);
                }
            } else {
                log.debug("No results found.");
            }
            log.debug("\nQuery successfully executed.");
            if (CollectionUtils.isEmpty(pulishers)) {
                throw new BusinessException(ErrorCode.ERROR_DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_DATA_NOT_FOUND);
        }
        return pulishers;
    }

    private JobInfo createJob(String sobjectType, BulkConnection connection)
            throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setObject(sobjectType);
        job.setOperation(OperationEnum.insert);
        job.setContentType(ContentType.CSV);
        job = connection.createJob(job);
        log.debug("[OPEN-SERVICE] ************* Create Job ************* ");
        System.out.println(job);
        log.debug("[OPEN-SERVICE] ************* Create Job ************* ");
        return job;
    }

    private List<BatchInfo> createBatchesFromCSVFile(
            BulkConnection connection, JobInfo jobInfo, String csvFileName) throws IOException {
        List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
        BufferedReader rdr =
                new BufferedReader(
                        new InputStreamReader(new FileInputStream(new File(csvFileName))));
        // read the CSV header row
        byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
        int headerBytesLength = headerBytes.length;
        File tmpFile = File.createTempFile("bulkAPIInsert", ".csv");

        // Split the CSV file into multiple batches
        try {
            FileOutputStream tmpOut = new FileOutputStream(tmpFile);
            int maxBytesPerBatch = 10000000; // 10 million bytes per batch
            int maxRowsPerBatch = 10000; // 10 thousand rows per batch
            int currentBytes = 0;
            int currentLines = 0;
            String nextLine;
            while ((nextLine = rdr.readLine()) != null) {
                byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
                // Create a new batch when our batch size limit is reached
                if (currentBytes + bytes.length > maxBytesPerBatch
                        || currentLines > maxRowsPerBatch) {
                    createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
                    currentBytes = 0;
                    currentLines = 0;
                }
                if (currentBytes == 0) {
                    tmpOut = new FileOutputStream(tmpFile);
                    tmpOut.write(headerBytes);
                    currentBytes = headerBytesLength;
                    currentLines = 1;
                }
                tmpOut.write(bytes);
                currentBytes += bytes.length;
                currentLines++;
            }
            // Finished processing all rows
            // Create a final batch for any remaining data
            if (currentLines > 1) {
                createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
            }
        } catch (AsyncApiException e) {
            e.printStackTrace();
        } finally {
            tmpFile.delete();
        }
        return batchInfos;
    }

    private void createBatch(
            FileOutputStream tmpOut,
            File tmpFile,
            List<BatchInfo> batchInfos,
            BulkConnection connection,
            JobInfo jobInfo)
            throws IOException, AsyncApiException {
        tmpOut.flush();
        tmpOut.close();
        FileInputStream tmpInputStream = new FileInputStream(tmpFile);
        try {
            BatchInfo batchInfo = connection.createBatchFromStream(jobInfo, tmpInputStream);
            System.out.println(batchInfo);
            batchInfos.add(batchInfo);

        } finally {
            tmpInputStream.close();
        }
    }

    private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setId(jobId);
        job.setState(JobStateEnum.Closed);
        connection.updateJob(job);
    }

    private void awaitCompletion(
            BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList)
            throws AsyncApiException {
        long sleepTime = 0L;
        Set<String> incomplete = new HashSet<String>();
        for (BatchInfo bi : batchInfoList) {
            incomplete.add(bi.getId());
        }
        while (!incomplete.isEmpty()) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            System.out.println("Awaiting results..." + incomplete.size());
            sleepTime = 10000L;
            BatchInfo[] statusList = connection.getBatchInfoList(job.getId()).getBatchInfo();
            for (BatchInfo b : statusList) {
                if (b.getState() == BatchStateEnum.Completed
                        || b.getState() == BatchStateEnum.Failed) {
                    if (incomplete.remove(b.getId())) {
                        System.out.println("BATCH STATUS:\n" + b);
                    }
                }
            }
        }
    }

    private void checkResults(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList)
            throws AsyncApiException, IOException {
        // batchInfoList was populated when batches were created and submitted
        for (BatchInfo b : batchInfoList) {
            InputStream inputStream = connection.getBatchResultStream(job.getId(), b.getId());
            CSVReader rdr = new CSVReader(inputStream);
            List<String> resultHeader = rdr.nextRecord();
            int resultCols = resultHeader.size();

            List<String> row;
            while ((row = rdr.nextRecord()) != null) {
                Map<String, String> resultInfo = new HashMap<String, String>();
                for (int i = 0; i < resultCols; i++) {
                    resultInfo.put(resultHeader.get(i), row.get(i));
                }
                boolean success = Boolean.valueOf(resultInfo.get("Success"));
                boolean created = Boolean.valueOf(resultInfo.get("Created"));
                String id = resultInfo.get("Id");
                String error = resultInfo.get("Error");
                if (success && created) {
                    System.out.println("Created row with id " + id);
                } else if (!success) {
                    System.out.println("Failed with error: " + error);
                }
            }
        }
    }
}
