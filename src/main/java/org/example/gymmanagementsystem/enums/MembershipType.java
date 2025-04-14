package org.example.gymmanagementsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MembershipType {
    STANDARD(8, "Up to 8 entries per month, access to basic fitness equipment"),
    STANDARD_PRO(12, "Up to 12 entries per month, access to basic fitness equipment"),
    PREMIUM(30, "Unlimited entries per month, group classes, sauna, and pool access");

    private final int monthlyEntries;
    private final String description;


}
