// UI elements
var donateSection = document.getElementById('donate');
var donateButton = document.getElementById('donate-button');
var unsupportedMsg = document.getElementById('unsupported');
var successMsg = document.getElementById('success');
var errorMsg = document.getElementById('error');

// Configuration for our payment
var methodData = [{
  // We're taking a card payment. Other options should come in the future.
  supportedMethods: ['basic-card'],
  data: {
    // Example payment networks
    supportedNetworks: ['visa', 'mastercard', 'amex'],
    supportedTypes: ['debit', 'credit']
  }
}];

var details = {
  total: {label: 'Test payment', amount: {currency: 'GBP', value: '0.99'}},
  displayItems: [
    {
      label: 'Test item',
      amount: {currency: 'GBP', value: '0.83'}
    },
    {
      label: 'Test tax',
      amount: {currency: 'GBP', value: '0.17'}
    }
  ],
  // If you include requestShipping then you need at least one shipping option
  // NB. If you have multiple options you can handle the selection by adding a
  // 'shippingoptionchange' event handler 
  shippingOptions: [
    {
      id: 'free',
      label: 'Free shipping',
      amount: {currency: 'GBP', value: '0.00'},
      selected: true,
    }
  ]
};
var options = { 
    requestPayerName: true,
    requestPayerEmail: true, 
    requestPayerPhone: true,
    requestShipping: true,
    shippingType: 'delivery'
  };

var paymentRequest;

// Check that the Payment Request API is available on this device
if (window.PaymentRequest) {
  // Initialise the PaymentRequest with our configuration
  paymentRequest = new PaymentRequest(methodData, details, options);
  enablePaymentUI(); 
} else {
  disablePaymentUI();  
}

function enablePaymentUI() {
  donateSection.style.display = 'block';
  unsupportedMsg.style.display = 'none';
}

function disablePaymentUI() {
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

  paymentRequest.addEventListener('shippingaddresschange', function(evt) {
    // Omitting for this demo, but we could process any changes to 
    // e.g. delivery costs, due to the shipping address changing here.
    console.log('Shipping address changed', evt);
  });

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
