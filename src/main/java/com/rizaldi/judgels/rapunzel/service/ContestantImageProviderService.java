package com.rizaldi.judgels.rapunzel.service;

import com.google.common.collect.Sets;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ContestantImageProviderService {
    private static final Logger LOG = LoggerFactory.getLogger(ContestantImageProviderService.class);
    private final Map<String, Set<String>> institutionNameMap = new HashMap<String, Set<String>>() {{
        put("UI", Sets.newHashSet("BersawReborn", "ImproveBySubmission", "Kembalinya Teman Bali Kami"));
        put("UGM", Sets.newHashSet("Sayata Kid Prims Sieve"));
        put("ITS", Sets.newHashSet("sudahbersatu"));
        put("ITB", Sets.newHashSet("Arurange Cozy Party", "Hikikomori", "I See the One", "sabeb aja namanya"));
        put("BINUS", Sets.newHashSet("Convex Coach", "JVC", "Zabivaka"));
    }};
    private final Map<String, String> institutionImageMap = new HashMap<String, String>() {{
        put("UI", "https://image.ibb.co/eDTzNf/UI.png");
        put("UGM", "https://image.ibb.co/bWeTF0/UGM.png");
        put("ITS", "https://image.ibb.co/gNFqTL/ITS.png");
        put("ITB", "https://image.ibb.co/iGfPoL/ITB.png");
        put("BINUS", "https://image.ibb.co/bYujoL/BINUS.png");
    }};
    private final String defaultImage = "https://image.ibb.co/nGB47f/INDONESIA.png";

    public void updateProfilePicture(User user) {
        String image = findImage(user);
        LOG.info("override {} profile picture. {} -> {}", user.getName(), user.getProfilePictureUrl(), image);
        user.setProfilePictureUrl(image);
    }

    private String findImage(User user) {
        String institution = findInstitution(user);
        return institutionImageMap.getOrDefault(institution, defaultImage);
    }

    private String findInstitution(User user) {
        for (String institution : institutionNameMap.keySet()) {
            boolean isAffiliated = institutionNameMap.get(institution).contains(user.getName());
            if (isAffiliated) return institution;
        }
        return null;
    }
}
