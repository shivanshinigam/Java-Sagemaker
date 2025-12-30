package com.shivanshi.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemaker.SageMakerClient;
import software.amazon.awssdk.services.sagemaker.model.*;

import java.util.Map;

public class RunPythonFromS3 {

    public static void main(String[] args) {

        SageMakerClient client = SageMakerClient.builder()
                .region(Region.US_EAST_1)
                .build();

        CreateTrainingJobRequest request =
                CreateTrainingJobRequest.builder()
                        .trainingJobName("numpy-s3-job-" + System.currentTimeMillis())

                        .roleArn("arn:aws:iam::780167008601:role/AmazonSageMaker-ExecutionRole-JavaS3")

                        .algorithmSpecification(
                                AlgorithmSpecification.builder()
                                        .trainingImage(
                                            "683313688378.dkr.ecr.us-east-1.amazonaws.com/sagemaker-scikit-learn:1.2-1-cpu-py3"
                                        )
                                        .trainingInputMode(TrainingInputMode.FILE)
                                        .build()
                        )

                        .hyperParameters(Map.of(
                                "sagemaker_program", "runner.py",
                                "sagemaker_submit_directory",
                                "s3://sagemaker-python-scripts-shivanshi/code.tar.gz"
                        ))

                        .inputDataConfig(
                                Channel.builder()
                                        .channelName("training")
                                        .dataSource(
                                                DataSource.builder()
                                                        .s3DataSource(
                                                                S3DataSource.builder()
                                                                        .s3DataType(S3DataType.S3_PREFIX)
                                                                        .s3Uri("s3://sagemaker-python-scripts-shivanshi/")
                                                                        .s3DataDistributionType(
                                                                                S3DataDistribution.FULLY_REPLICATED
                                                                        )
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )

                        .resourceConfig(
                                ResourceConfig.builder()
                                        .instanceType(TrainingInstanceType.ML_M5_LARGE)
                                        .instanceCount(1)
                                        .volumeSizeInGB(5)   // ✅ FIXED
                                        .build()
                        )

                        .stoppingCondition(
                                StoppingCondition.builder()
                                        .maxRuntimeInSeconds(600)
                                        .build()
                        )

                        .outputDataConfig(
                                OutputDataConfig.builder()
                                        .s3OutputPath("s3://sagemaker-python-scripts-shivanshi/output/")
                                        .build()
                        )
                        .build();

        client.createTrainingJob(request);
        client.close();

        System.out.println("Training job started successfully.");
    }

    // ✅ ADD THE METHOD HERE (INSIDE CLASS, OUTSIDE main)

    public static void runWithUserScript(String scriptS3Path) {

        SageMakerClient client = SageMakerClient.builder()
                .region(Region.US_EAST_1)
                .build();

        CreateTrainingJobRequest request =
                CreateTrainingJobRequest.builder()
                        .trainingJobName("ai-job-" + System.currentTimeMillis())

                        .roleArn("arn:aws:iam::780167008601:role/AmazonSageMaker-ExecutionRole-JavaS3")

                        .algorithmSpecification(
                                AlgorithmSpecification.builder()
                                        .trainingImage(
                                            "683313688378.dkr.ecr.us-east-1.amazonaws.com/sagemaker-scikit-learn:1.2-1-cpu-py3"
                                        )
                                        .trainingInputMode(TrainingInputMode.FILE)
                                        .build()
                        )

                        .hyperParameters(Map.of(
                                "sagemaker_program", "runner.py",
                                "sagemaker_submit_directory",
                                "s3://sagemaker-python-scripts-shivanshi/code.tar.gz"
                        ))

                        // 🔥 pass uploaded script path to runner.py
                        .environment(Map.of(
                                "USER_SCRIPT_S3_PATH", scriptS3Path
                        ))

                        .inputDataConfig(
                                Channel.builder()
                                        .channelName("training")
                                        .dataSource(
                                                DataSource.builder()
                                                        .s3DataSource(
                                                                S3DataSource.builder()
                                                                        .s3DataType(S3DataType.S3_PREFIX)
                                                                        .s3Uri("s3://sagemaker-python-scripts-shivanshi/")
                                                                        .s3DataDistributionType(
                                                                                S3DataDistribution.FULLY_REPLICATED
                                                                        )
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )

                        .resourceConfig(
                                ResourceConfig.builder()
                                        .instanceType(TrainingInstanceType.ML_M5_LARGE)
                                        .instanceCount(1)
                                        .volumeSizeInGB(5)
                                        .build()
                        )

                        .stoppingCondition(
                                StoppingCondition.builder()
                                        .maxRuntimeInSeconds(600)
                                        .build()
                        )

                        .outputDataConfig(
                                OutputDataConfig.builder()
                                        .s3OutputPath("s3://sagemaker-python-scripts-shivanshi/output/")
                                        .build()
                        )
                        .build();

        client.createTrainingJob(request);
        client.close();

        System.out.println("SageMaker job started for user script.");
    }
}
