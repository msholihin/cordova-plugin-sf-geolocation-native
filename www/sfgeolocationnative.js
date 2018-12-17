/**
 *  latitude and longitude to find location address.
 * @param {*} success {PositionResult[]} Success callback containing array of result objects
 * @param {*} error Error callback
 */
var sfgeolocation = {
  getCurrentLocation: function (success, error) {
    cordova.exec(success, error, 'SFGeolocationNative', 'getCurrentLocation', []);
  }
}

cordova.addConstructor(function () {
  if (!window.plugins) {window.plugins = {};}

  window.plugins.sfgeolocationnative = sfgeolocation;
  return window.plugins.sfgeolocationnative;
});

/*
PositionResult:
- latitude
- longitude
- accuracy
- location_provider
*/