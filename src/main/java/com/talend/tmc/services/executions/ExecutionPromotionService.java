package com.talend.tmc.services.executions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talend.tmc.dom.*;
import com.talend.tmc.services.*;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;

public class ExecutionPromotionService {
    private final String path = "executions/promotions";
    private final TalendApiClient client;
    private final TalendCloudRegion region;
    private ObjectMapper mapper;

    public static ExecutionPromotionService instance(TalendCredentials credentials, TalendCloudRegion region) throws NullPointerException
    {
        if (region == null) throw new NullPointerException("TalendCloudRegion cannot be null");
        if (credentials == null) throw new NullPointerException("TalendCredentials cannot be null");
        ExecutionPromotionService _instance = new ExecutionPromotionService(credentials, region);

        return _instance;
    }

    private ExecutionPromotionService(TalendCredentials credentials, TalendCloudRegion region) {
        this.client = TalendApiClient.createNewInstance(credentials);
        this.region = region;
        // Set ObjectMapper
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ExecutionResponse post(ExecutionPromotionRequest request) throws TalendRestException, IOException,
            NullPointerException
    {
        if (request == null) throw new NullPointerException("ExecutionPromotionRequest cannot be null");
        Writer jsonWriter = new StringWriter();
        mapper.writeValue(jsonWriter, request);
        jsonWriter.flush();
        StringBuilder uri = new StringBuilder();
        uri.append(region.toString()+path);

        ExecutionResponse executionResponse = null;

        Hashtable<Integer, String> response = client.call(HttpMethod.POST, uri.toString(), jsonWriter.toString());

        for (Integer httpStatus : response.keySet())
        {
            String payload = response.get(httpStatus);
            if (httpStatus != 201)
            {
                TalendError error = mapper.readValue(payload, TalendError.class);
                throw new TalendRestException(error.toString());
            } else {
                executionResponse = mapper.readValue(payload, ExecutionResponse.class);
            }

        }

        return executionResponse;
    }


    public ExecutionPromotionResponse get(String id) throws TalendRestException, IOException,
            NullPointerException
    {
        ExecutionPromotionResponse execution = null;
        if (id == null) throw new NullPointerException("id of Execution Job cannot be null");
        StringBuilder uri = new StringBuilder();
        uri.append(region.toString()+path+"/"+id);

        Hashtable<Integer, String> response = client.call(HttpMethod.GET, uri.toString(), null);

        for (Integer httpStatus : response.keySet())
        {
            String payload = response.get(httpStatus);
            if (httpStatus != 200)
            {
                TalendError error = mapper.readValue(payload, TalendError.class);
                throw new TalendRestException(error.toString());
            } else {
                execution = mapper.readValue(payload, ExecutionPromotionResponse.class);
            }

        }

        return execution;
    }
}
