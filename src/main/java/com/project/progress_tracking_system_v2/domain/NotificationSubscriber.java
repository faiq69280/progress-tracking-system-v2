package com.project.progress_tracking_system_v2.domain;

import com.project.progress_tracking_system_v2.constants.ContactType;

import java.util.EnumMap;

public interface NotificationSubscriber {
    public EnumMap<ContactType, String> getSubscriptionInformation();
}
