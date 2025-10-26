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

    public String uploadAudioFile(byte[] fileBytes, String originalFilename) throws IOException {
        // Gửi kèm metadata để Cloudinary giữ tên gốc
        Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", "video",  // Cloudinary dùng "video" cho audio files
                "use_filename", true,      // Giữ nguyên tên gốc
                "unique_filename", false,  // Không random thêm hậu tố
                "folder", "music_uploads"  // (tuỳ chọn) gom file vào thư mục
        );

        Map uploadResult = cloudinary.uploader().upload(fileBytes, params);
        return (String) uploadResult.get("secure_url");
    }
}
