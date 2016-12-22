// UI elements
var donateSection = document.getElementById('donate');
var donateButton = document.getElementById('donate-button');
var unsupportedMsg = document.getElementById('unsupported');
var successMsg = document.getElementById('success');
var errorMsg = document.getElementById('error');

// Configuration for our payment (other payment providers are available too 😄)
var methodData = [{supportedMethods: ['visa', 'mastercard', 'amex']}];
var details = {total: {label: 'Test payment', amount: {currency: 'GBP', value: '1.00'}}};

// Check that the Payment Request API is available on this device
if (window.PaymentRequest) {
  // Show donate button
  donateSection.style.display = 'block';
  unsupportedMsg.style.display = 'none';
}
else {
  // Show unsupported message
  donateSection.style.display = 'none';
  unsupportedMsg.style.display = 'block';
}

/**
 * Here is where we would send the payment info to the server / payment gateway for processing,
 * but I'm not quite ready to take real money from you yet 😉 Simulating by just waiting 2 secs.
 */
function processPaymentDetails(uiResult) {
  return new Promise(function (resolve) {
    setTimeout(function() {
      resolve(uiResult);
    }, 2000);
  });
}

function showSuccess() {
  donateButton.style.display = 'none';
  errorMsg.style.display = 'none';
  successMsg.style.display = 'block';
}

function showError() {
  donateButton.style.display = 'none';
  errorMsg.style.display = 'block';
  successMsg.style.display = 'none';
}

function onDonateButtonClick() {

  // Initialise the PaymentRequest with our configuration
  // We could also pass in additional options as a 3rd parameter here, such as:
  // {requestShipping: true, requestPayerEmail: true, requestPayerPhone: true};
  var paymentRequest = new PaymentRequest(methodData, details);

  // Show the native UI
  paymentRequest.show()
    .then(function(uiResult) {
      processPaymentDetails(uiResult)
        .then(function(uiResult) {
          uiResult.complete('success');
          showSuccess();
        });
    })
    .catch(function(error) {
      console.warn('Unable to complete purchase', error);
      // D'oh. Inform the user the purchase could not be completed...
      showError();
    });
}

donateButton.addEventListener('click', onDonateButtonClick);
