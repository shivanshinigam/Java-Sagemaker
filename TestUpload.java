package com.shivanshi.aws;

public class TestUpload {

    public static void main(String[] args) throws Exception {

        String codeFromUi =
                "print('Hello from AI code')\n" +
                "#ai";

        AiCodeService service = new AiCodeService();
        String s3Path = service.uploadCodeToS3(codeFromUi);

        System.out.println("Uploaded to: " + s3Path);
    }
}
