package iuh.fit.MusicStreamAppBackEnd.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    @Autowired
    private Cloudinary cloudinary;

    // Thay MultipartFile thành byte[]
    public String uploadAudioFile(byte[] fileBytes) throws IOException {
        Map params = ObjectUtils.asMap(
                "resource_type", "video"
        );

        Map uploadResult = cloudinary.uploader().upload(fileBytes, params);
        return (String) uploadResult.get("secure_url");
    }
}