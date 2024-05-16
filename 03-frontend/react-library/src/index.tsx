import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { BrowserRouter } from 'react-router-dom'
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';


const stripePromise = loadStripe('pk_test_51PGe5YSBetCLCCAsZ3covbih4EHh4LsC0g31gWgjgTd5OC4tw35pF20axQtJ9ZXxfVRYkv9udP5r9dTsw0ww9a16004bXO7CfL');


const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <BrowserRouter>
  <Elements stripe={stripePromise}>
  <App />
  </Elements>
  </BrowserRouter>
);

