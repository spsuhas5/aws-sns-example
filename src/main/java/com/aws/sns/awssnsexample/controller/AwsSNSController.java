package com.aws.sns.awssnsexample.controller;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsSNSController {

    public AmazonSNSClient amazonSNSClient;

    @Autowired
    public AwsSNSController(AmazonSNSClient amazonSNSClient) {
        this.amazonSNSClient = amazonSNSClient;
    }

    private String Topic_ARN = "arn:aws:sns:us-east-1:711392004085:my-sns-topic";

    private String Topic_ARN_SMS = "arn:aws:sns:us-east-1:711392004085:aws-sns-sms-topic";

    @GetMapping("addSubscription/{email}")
    public String addSubscription(@PathVariable String email) {
        SubscribeRequest subscribeRequest = new SubscribeRequest(Topic_ARN, "email", email);
        amazonSNSClient.subscribe(subscribeRequest);
        return "Your Subscription request is pending. To confirm the confirm the subscription, please" +
                " check your email: "+email;
    }

    //TODO: did not get notification to mobile phone, need to look into this
    @GetMapping("addPhoneSubscription/{phone}")
    public String addSMSSubscription(@PathVariable String phone) {
        SubscribeRequest subscribeRequest = new SubscribeRequest(Topic_ARN_SMS, "SMS", phone);
        amazonSNSClient.subscribe(subscribeRequest);
        return "Your Subscription request is pending. To confirm the confirm the subscription, please" +
                " check your email: "+phone;
    }

    @GetMapping("/sendNotification")
    public String publishMessageToTopic() {
        PublishRequest publishRequest = new PublishRequest(Topic_ARN, buildEmailBody(), "Notification: N/w connectivity issue");
        amazonSNSClient.publish(publishRequest);
        return "Notification sent successfully !!";
    }

    private String buildEmailBody() {
        return "Dear Employee, Connection is down in Bangalore right now. " +
                "All the servers in bangalore data center are not accessible. " +
                "We are working on it. " +
                "Notification will be sent out as the issue is resolved." +
                "For any questions please feel free to contact IT service support team.";
    }
}
