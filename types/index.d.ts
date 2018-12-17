// Type definitions for SF Geolocation Native Plugin
// Project: http://10.1.35.35:8484/backoffice/mobile/cordova-plugin-sf-geolocation-native.git
// Definitions by: PT. Smartfren Telecom
// Definitions: <not specified>
// 
// Copyright (c) Microsoft Open Technologies Inc
// Licensed under the MIT license 

/**
 * This plugin used for get device current location.
 */
interface SFGeolocationNative {
    /** Indicates that Cordova initialize successfully. */
    available: boolean;
    /** Device latitude position */
    latitude: number;
    /** Device longitude position */
    longitude: number;
    /** Device position accuracy */
    accuracy: number;
    /** Device location provider */
    location_provider: string;
}

declare var sfgeolocationnative: SFGeolocationNative;