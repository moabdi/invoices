package com.poc.invoices.services;

import java.io.File;

public interface AWSS3Service {

    void upload(File file, String buketName);

    byte[] download(String fileName, String buketName);

}
