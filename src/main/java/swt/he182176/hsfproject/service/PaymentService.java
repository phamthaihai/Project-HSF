package swt.he182176.hsfproject.service;

import swt.he182176.hsfproject.entity.Enrollment;

import java.util.Map;

public interface PaymentService {

    Map<String, String> createPayment(Enrollment enrollment);

}