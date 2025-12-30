package com.shivanshi.aws;

public class TestUploadAndRun {

    public static void main(String[] args) throws Exception {

        String codeFromUi =
                "print('HELLO FROM UI')\n" +
                "print(10 + 20)\n" +
                "#ai";

        AiCodeService service = new AiCodeService();
        service.uploadAndRun(codeFromUi);
    }
}
