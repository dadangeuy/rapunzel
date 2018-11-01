package com.rizaldi.judgels.rapunzel.service;

import com.google.common.collect.Sets;
import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ImageProviderService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageProviderService.class);
    private final Map<String, Set<String>> institutionNameMap = new HashMap<String, Set<String>>() {{
        put("UI", Sets.newHashSet("BersawReborn", "ImproveBySubmission", "Kembalinya Teman Bali Kami"));
        put("UGM", Sets.newHashSet("Sayata Kid Prims Sieve", "Sayata Kid Prim's Sieve"));
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

    public void update(List<ScoreboardRow> scoreboardRows) {
        scoreboardRows.forEach(this::update);
    }

    private void update(ScoreboardRow scoreboardRow) {
        String image = findImage(scoreboardRow.getContestant());
        if (image != null) scoreboardRow.setLogo(image);
    }

    private String findImage(String name) {
        String institution = findInstitution(name);
        return institutionImageMap.get(institution);
    }

    private String findInstitution(String name) {
        for (String institution : institutionNameMap.keySet()) {
            boolean isAffiliated = institutionNameMap.get(institution).contains(name);
            if (isAffiliated) return institution;
        }
        return null;
    }
}
