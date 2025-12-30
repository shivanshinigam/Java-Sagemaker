package com.shivanshi.aws;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AiCodeService {

    private static final String BUCKET_NAME =
            "sagemaker-python-scripts-shivanshi";

    // STEP 1: Validate that code has #ai
    private void validateAiCode(String code) {
        if (code == null || !code.contains("#ai")) {
            throw new RuntimeException("Code must contain #ai hashtag");
        }
    }

    // STEP 2: Write code to a temp .py file
    private File writeCodeToTempFile(String code) throws IOException {
        File file = File.createTempFile("ai_code_", ".py");
        FileWriter writer = new FileWriter(file);
        writer.write(code);
        writer.close();
        return file;
    }

    // STEP 3: Upload file to S3 and return S3 path
    public String uploadCodeToS3(String code) throws IOException {

        validateAiCode(code);

        File file = writeCodeToTempFile(code);

        String key = "ai-code/" + System.currentTimeMillis() + ".py";

        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .build(),
                RequestBody.fromFile(file)
        );

        return "s3://" + BUCKET_NAME + "/" + key;
    }
    public String uploadAndRun(String code) throws Exception {

        // Step 1: upload code to S3
        String s3Path = uploadCodeToS3(code);
        System.out.println("Code uploaded at: " + s3Path);

        // Step 2: run SageMaker and pass S3 path
        RunPythonFromS3.runWithUserScript(s3Path);

        return s3Path;
    }

}
