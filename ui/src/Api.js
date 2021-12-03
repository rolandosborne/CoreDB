const TEST=''//'https://dn33.diatum.io:4444/ssi/';

const FETCH_TIMEOUT = 15000;

function checkResponse(response) {
  if(response.status >= 400 && response.status < 600) {
    throw new Error(response.url + " failed");
  }
}

async function fetchWithTimeout(url, options) {
    return Promise.race([
        fetch(url, options).catch(err => { throw new Error(url + ' failed'); }),
        new Promise((_, reject) => setTimeout(() => reject(new Error(url + ' timeout')), FETCH_TIMEOUT))
    ]);
}

export async function getNodeAccounts(alias: string, password:string) {
  let response = await fetchWithTimeout(TEST + "portal/amigos?handle=" + encodeURIComponent(alias) + "&password=" + encodeURIComponent(password), 
      { method: 'GET', timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function getNodeProfile(alias: string, password:string, amigoId: string) {
  let response = await fetchWithTimeout(TEST + "portal/amigos/" + amigoId + "?handle=" + encodeURIComponent(alias) + "&password=" + encodeURIComponent(password), 
      { method: 'GET', timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function createAccountToken(alias: string, password:string) {
  let response = await fetchWithTimeout(TEST + "portal/account/?alias=" + encodeURIComponent(alias) + "&password=" + encodeURIComponent(password), 
      { method: 'POST', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function resetAccountToken(alias: string, password:string, amigoId: string) {
  let response = await fetchWithTimeout(TEST + "portal/account/?alias=" + encodeURIComponent(alias) + "&password=" + encodeURIComponent(password) + "&amigoId=" + amigoId, 
      { method: 'PUT', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function createAccount(username: string, password: string, token: string) {
  let response = await fetchWithTimeout(TEST + "portal/profile?username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password) + "&token=" + token, 
      { method: 'POST', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function deleteAccount(alias: string, password: string, amigoId: string) {
  let response = await fetchWithTimeout(TEST + "portal/amigos/" + amigoId + "?alias=" + encodeURIComponent(alias) + "&password=" + encodeURIComponent(password), 
      { method: 'DELETE', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
}

export async function resetAccount(password: string, token: string) {
  let response = await fetchWithTimeout(TEST + "portal/profile/password?password=" + encodeURIComponent(password) + "&token=" + token,
      { method: 'PUT', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function getProfile(username: string, password: string) {
  let response = await fetchWithTimeout(TEST + "portal/profile?handle=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password),
      { method: 'GET', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function checkToken(token: string) {
  let response = await fetchWithTimeout(TEST + "portal/account?token=" + token,
      { method: 'GET', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function checkUsername(username: string, amigoId: string) {
  let id = "";
  if(amigoId != null) {
    id = "&amigoId=" + amigoId;
  }
  let response = await fetchWithTimeout(TEST + "portal/username?username=" + username + id,
      { method: 'GET', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function updateUsername(username: string, password: string, update: string) {
  let response = await fetchWithTimeout(TEST + "portal/profile/login?username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password) + "&newUsername=" + encodeURIComponent(update),
      { method: 'PUT', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT } );
  checkResponse(response);
  return await response.json();
}

export async function checkAdmin() {
  let response = await fetchWithTimeout(TEST + "portal/admin", { method: 'GET', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT });
  checkResponse(response);
  return await response.json();
}

export async function setAdmin(username: string, password: string, domain: string) {
  let response = await fetchWithTimeout(TEST + "portal/admin?username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password) + "&domain=" + encodeURIComponent(domain), { method: 'PUT', headers: { 'Content-Type': 'application/json' }, timeout: FETCH_TIMEOUT });
  checkResponse(response);
}

