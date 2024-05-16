// This file contains all info to work with 3rd party service within okta, 
// To route our React App to service within Okta
//--------------LINKAGE CONFIG FROM REACT APP to OKTA APP---------------------
export const oktaConfig =  {
    clientId :'0oagxbv3w6y4hCbb55d7',
    issuer: 'https://dev-94338646.okta.com/oauth2/default',
    redirectUri: 'https://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,

}