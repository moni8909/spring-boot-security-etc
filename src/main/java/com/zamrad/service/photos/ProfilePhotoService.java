package com.zamrad.service.photos;

import com.zamrad.domain.Profile;
import com.zamrad.dto.Image;
import com.zamrad.repository.ProfileRepository;
import com.zamrad.service.artist.ProfileService;
import com.zamrad.service.fileuploader.PhotoUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class ProfilePhotoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProfilePhotoService.class);

    private final PhotoUploader photoUploader;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfilePhotoService(PhotoUploader photoUploader, ProfileRepository profileRepository, ProfileService profileService) {
        this.photoUploader = photoUploader;
        this.profileRepository = profileRepository;
    }

    public List<Image> setNewProfilePhoto(MultipartFile photo, Profile profile) {
        try {
            final URL originalImageUrl = photoUploader.upload(photo.getInputStream(), photo.getContentType(), photo.getSize());
            profile.setPhotoUrl(originalImageUrl.toString());

            profileRepository.save(profile);

            return createImage(originalImageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Upload failed: ", e);
        }
    }

    private List<Image> createImage(URL originalImageUrl) {
        final Image image = Image.builder()
                .original(true)
                .height(375)
                .width(272)
                .url(originalImageUrl.toString()).build();
        return Collections.singletonList(image);
    }

    public void createShowcase(MultipartFile[] photos, Principal principal) {

    }

    private void uploadAllPhotos(@RequestPart MultipartFile[] photos, @ApiIgnore Principal principal) {
        final List<Image> uploadedImages = Arrays.stream(photos)
                .map(photo -> uploadPhoto(photo, principal))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(toList());
    }

    private List<Image> uploadPhoto(MultipartFile photo, Principal principal) {
        final Long size = photo.getSize();
        final String contentType = photo.getContentType();

        try {
            final URL originalUrl = photoUploader.upload(photo.getInputStream(), photo.getContentType(), photo.getSize());
            return createImage(originalUrl);
        } catch (IOException e) {
            LOGGER.error("Image input stream could not be read: ", e);
            return Collections.emptyList();
        }
    }

    /**
     * TODO: Update a profile's showcase.
     */
    public void updateShowcase() {

    }
}
