self.addEventListener('paymentrequest', function(paymentrequestEvent) {
  
  console.log('paymentrequest event', paymentrequestEvent.data);

  paymentrequestEvent.respondWith(new Promise(function(resolve, reject) {

    try {
      // Retrive payment card options
      var optionIds = paymentrequestEvent.data.optionId.split(';');

      console.log('optionIds', optionIds);

      // Build Basic-Card response according to the specific BasicCardResponse dictionary model
      var basicCardResponse = {
        methodName: 'basic-card',
        details: {
          cardholderName: optionIds[0],
          cardNumber: optionIds[1],
          expiryMonth: optionIds[2],
          expiryYear: optionIds[3],
          cardSecurityCode: optionIds[4],
          billingAddress: null
        }
      };

      resolve(basicCardResponse);

    } catch (error) { 
      reject(error); 
    }

  }));

});