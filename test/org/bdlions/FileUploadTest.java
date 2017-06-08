/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions;

import java.io.File;
import org.bdlions.client.reqeust.uploads.UploadService;

/**
 *
 * @author alamgir
 */
public class FileUploadTest {
    public static void main(String[] args) {
        try {
            System.out.println(UploadService.uploadImage("http://localhost:8084/FlatAuction/", new File("C:\\Users\\alamgir\\Desktop\\20170227_181434.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
