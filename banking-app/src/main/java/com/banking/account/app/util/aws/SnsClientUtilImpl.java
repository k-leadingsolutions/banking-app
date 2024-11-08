package com.banking.account.app.util.aws;


import com.banking.account.app.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.regions.Region;

@Slf4j
@Component
public class SnsClientUtilImpl implements SnsClientUtil {

    @Value("$aws.sns.region")
    private String awsRegion;

    @Value("$aws.topic.name")
    private String awsTopicName;

    @Value("$aws.sns.topicArn")
    private String awsTopicArn;

    @Value("$aws.access.key.id")
    private String awsAccountId;

    @Override
    public void publish(String message) {
        String snsTopicArn = awsTopicArn + awsRegion + awsAccountId + awsTopicName;
        try {
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(message)
                    .topicArn(snsTopicArn)
                    .build();

            PublishResponse publishResponse = this.getSnsClient().publish(publishRequest);
            log.info("Message successfully published with message id: {}", publishResponse.messageId());
        } catch (SnsException e) {
            log.error("Message successfully published. {}", e.getMessage());
            throw new ServiceException("Message successfully published. {}" + e.getMessage());

        }

    }

    private SnsClient getSnsClient() {
        return SnsClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.of(awsRegion))
                .build();
    }
}
