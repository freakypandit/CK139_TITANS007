package com.hack.thirstymission.cloudanchors;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment{

    @Override
    protected Config getSessionConfiguration(Session session) {
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = super.getSessionConfiguration(session);
        config.setCloudAnchorMode(Config.CloudAnchorMode.ENABLED);
        return config;
    }
}
