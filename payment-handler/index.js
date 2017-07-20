function register() {

  if (navigator.serviceWorker) {

    navigator.serviceWorker.register('service-worker.js')
      .then(function(registration) {

        if (registration.paymentAppManager) {
          
          /*
          registration.paymentAppManager.options.set('-key', {
            enabledMethods: ['basic-card'],
            name: 'Card Provider Demo',
            id: 'P J TESTER;4111111111111111;01;25;123'
          });
          */

          registration.paymentAppManager.setManifest({
            name: 'CardProviderDemo',
            options: [{
              enabledMethods: ['basic-card'],
              name: 'Card Provider Demo',
              id: 'P J TESTER;4111111111111111;01;25;123'
            }]
          });

        } else {
          console.warn('Payment Handler API not supported');
        }

        return registration;

      }).then(function(sw) {
        console.log('Service worker', sw);
      }).catch(function(error) {
        console.error('Error registering service worker', error);
      });

  }

}

