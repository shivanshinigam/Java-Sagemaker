package com.shivanshi.aws;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AiCodeController {

    private final AiCodeService aiCodeService = new AiCodeService();

    @PostMapping("/run-ai-code")
    public String runAiCode(@RequestBody String code) throws Exception {

        aiCodeService.uploadAndRun(code);

        return "AI code submitted successfully";
    }
}
