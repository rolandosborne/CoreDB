import './App.css';
import avatar from './asset/avatar.png';
import React, { useRef, useState, useEffect } from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useLocation
} from "react-router-dom";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import Modal from 'react-bootstrap/Modal';
import { checkAdmin, setAdmin, updateUsername, getProfile, checkUsername, checkToken, resetAccount, deleteAccount, createAccount, getNodeAccounts, getNodeProfile, createAccountToken, resetAccountToken } from './Api.js';
import { FaUserPlus, FaUserAltSlash, FaRegCopy, FaEdit } from "react-icons/fa";
import { RiRotateLockLine } from "react-icons/ri";
import { BsArrowRepeat } from "react-icons/bs";

function App() {
  return (
    <Router>
      <div>
        <Switch>
          <Route path="">
            <Root />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function Root() {
  let [view, setView] = useState(null);

  let query = useQuery();
  useEffect(() => {
    setView(query.get("view"));
  }, [query]);

  if(view === 'account') {
    return <Account />;
  }
  else {
    return <Admin />;
  }
}

function Header() {
  return (<></>);
}

function Footer() {
  return (<></>);
}

function Admin() {
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [domain, setDomain] = useState('');
  const [ids, setIds] = useState(null);
  const [amigos, setAmigos] = useState([]);
  const [showCreate, setShowCreate] = useState(false); 
  const [showReset, setShowReset] = useState(false); 
  const [showDelete, setShowDelete] = useState(false); 
  const [createToken, setCreateToken] = useState(null);
  const [resetToken, setResetToken] = useState(null);
  const [deleteAmigo, setDeleteAmigo] = useState({});
  const [params, setParams] = useState(null);
 
  let profiles = useRef([]);

  const onLogin = async () => {
    await getProfiles();
  }

  useEffect(() => {
    checkAdmin().then(flag => {
      setParams(flag);
    });
  }, []);

  const getProfiles = async () => {
    try {
      let ids = await getNodeAccounts(name, password);
      setIds(ids);

      // populate profile list
      profiles.current = [];
      for(let i = 0; i < ids.length; i++) {
        let p = await getNodeProfile(name, password, ids[i]);
        profiles.current.push(p);
        setAmigos([...profiles.current]);
      }
    }
    catch(err) {
      console.log(err);
      alert("failed to retrieve accounts");
    }
  }

  const Item = (props) => {

    const Handle = () => {
      if(props.value.handle === null) {
        return (<span style={{ color: '#888888' }}>Unset Handle</span>);
      }
      return (<span>{ props.value.handle }</span>);
    }
   
    const Name = () => {
      if(props.value.name == null) {
        return (<span style={{ color: '#888888' }}>Unset Name</span>);
      }
      return (<span>{ props.value.name }</span>);
    }

    let source = avatar;
    if(props.value.logo != null) {
      source = "data:image/png;base64," + props.value.logo;
    }

    return (
      <div>
        <div style={{ display: 'flex', flexDirection: 'row', padding: 8, borderBottom: '1px solid #dddddd' }}>
          <img src={source} alt='' style={{ display: 'flex', borderRadius: 8, width: 64, height: 64 }} />
          <div style={{ display: 'flex', flexDirection: 'column', flexGrow: 1, paddingLeft: 24 }}>
            <div style={{ display: 'flex', fontSize: 16 }}><Name /></div>
            <div style={{ display: 'flex', fontSize: 16 }}><Handle /></div>
            <div style={{ display: 'flex', flexGrow: 1, fontSize: 12, alignItems: 'flex-end', lineBreak: 'anywhere' }}>
	      <span style={{ width: '100%' }}>{ props.value.amigoId }</span>
	    </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', marginRight: 16 }}>
            <div style={{ padding: 8, border: '1px solid #aaaaaa', borderRadius: 8, backgroundColor: '#eeeeee' }}>
              <FaUserAltSlash style={{ cursor: 'pointer', color: '#ff5733', fontSize: 20, marginRight: 16 }} onClick={() => onDeleteAccount(props.value)} />
              <RiRotateLockLine style={{ cursor: 'pointer', width: 24, height: 24, color: '#ff7900' }} onClick={() => onResetAccount(props.value.amigoId)} />
            </div>
          </div>
        </div>
        <ResetAccount />
        <DeleteAccount />
      </div>
    );
  }

  const onCopy = (url) => {
    var clipArea = document.createElement("textarea");
    document.body.appendChild(clipArea);
    clipArea.value = url;
    clipArea.select();
    document.execCommand("copy");
    document.body.removeChild(clipArea);
  }

  const onCopyCreate = () => {
    let url = window.location.href.split('?')[0].replace(/\/$/, '') + '?view=account&create=' + createToken;
    onCopy(url);
  }

  const onCopyReset = () => {
    let url = window.location.href.split('?')[0].replace(/\/$/, '') + '?view=account&reset=' + resetToken;
    onCopy(url);
  }
 
  const onDeleteConfirm = async () => {
    try {
      await deleteAccount(name, password, deleteAmigo.amigoId);
      setShowDelete(false);
    }
    catch(err) {
      console.log(err);
      alert("failed to delete account");
    }
    await getProfiles();
  }
 
  const DeleteAccount = () => {

    const Name = () => {
      if(deleteAmigo.name == null) {
        return <div><span>Name: </span><span style={{ color: '#888888' }}>Not Set</span></div>;
      } 
      else {
        return <div>Name: { deleteAmigo.name }</div>
      }
    }
     
    const Username = () => {
      if(deleteAmigo.handle == null) {
        return <div><span>Username: </span><span style={{ color: '#888888' }}>Not Set</span></div>;
      } 
      else {
        return <div>Username: { deleteAmigo.handle }</div>
      }
    }
     
    return (
      <Modal show={showDelete} onHide={onCloseDeleteModal} size="lg" centered>
        <Modal.Header closeButton>
          <Modal.Title>Delete Account</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div>
              <div style={{ fontSize: 18, paddingBottom: 16 }}>Are you sure you want to delete the following account?</div>
              <Name />
              <Username />
              <div style={{ lineBreak: 'anywhere' }}>ID: { deleteAmigo.amigoId }</div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="danger" onClick={onDeleteConfirm}>Yes, Delete</Button>
          <Button variant="secondary" onClick={onCloseDeleteModal}>Cancel</Button>
        </Modal.Footer>
      </Modal>
    );
  }

  const CreateAccount = () => {
    return (
      <Modal show={showCreate} onHide={onCloseCreateModal} size="xl" centered>
        <Modal.Header closeButton>
          <Modal.Title>Create Account Link</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ display: 'flex', flexDirection: 'column', padding: 32, width: '100%' }}>
              <div style={{ fontSize: 18, marginBottom: 8 }}>The following link will create an account in this node:</div>
              <div style={{ borderRadius: 4, border: '1px solid #aaaaaa', display: 'flex' }}>
                <div style={{ padding: 8, backgroundColor: '#eeeeee', lineBreak: 'anywhere', display: 'flex', flexGrow: 1 }}>
	    	  <span style={{ width: '100%' }}>{ window.location.href.split('?')[0].replace(/\/$/, '') + '?view=account&create=' + createToken }</span>
	        </div>
                <div style={{ padding: 8, borderLeft: '1px solid #aaaaaa', display: 'flex' }}>
                  <FaRegCopy style={{ fontSize: 20, color: '#0077CC', cursor: 'pointer' }} onClick={onCopyCreate} />
                </div>
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onCloseCreateModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  const ResetAccount = () => {
    return (
      <Modal show={showReset} onHide={onCloseResetModal} size="xl" centered>
        <Modal.Header closeButton>
          <Modal.Title>Reset Password Link</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ display: 'flex', flexDirection: 'column', padding: 32, width: '100%' }}>
              <div style={{ fontSize: 18, marginBottom: 8 }}>The following link will reset the account password:</div>
              <div style={{ borderRadius: 4, border: '1px solid #aaaaaa', display: 'flex' }}>
                <div style={{ padding: 8, backgroundColor: '#eeeeee', lineBreak: 'anywhere', display: 'flex', flexGrow: 1 }}>
	          <span style={{ width: '100%' }}>{ window.location.href.split('?')[0].replace(/\/$/, '') + '?view=account&reset=' + resetToken }</span>
	    	</div>
                <div style={{ padding: 8, borderLeft: '1px solid #aaaaaa', display: 'flex' }}>
                  <FaRegCopy style={{ fontSize: 20, color: '#0077CC', cursor: 'pointer' }} onClick={onCopyReset} />
                </div>
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onCloseResetModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  const Accounts = () => {
    if(ids.length === 0) {
      return (<div style={{ display: 'flex', width: '100%', alignItems: 'center', justifyContent: 'center' }}>
        <div style={{ display: 'flex' }}>No Accounts</div>
      </div>);
    }
    return (<div style={{ width: '100%' }}>
        {amigos.map((item) => <Item key={item.amigoId} value={item} />)}
    </div>);
  }

  const onCloseResetModal = () => {
    setShowReset(false);
  }

  const onCloseCreateModal = () => {
    setShowCreate(false);
  }

  const onCloseDeleteModal = () => {
    setShowDelete(false);
  }

  const onDeleteAccount = async (amigo) => {
    setDeleteAmigo(amigo);
    setShowDelete(true);
  }

  const onCreateAccount = async () => {
    try {
      setCreateToken(await createAccountToken(name, password));
      setShowCreate(true);
    }
    catch(err) {
      console.log(err);
      alert("failed to create account token"); 
    }
  }

  const onResetAccount = async (amigoId) => {
    try {
      setResetToken(await resetAccountToken(name, password, amigoId));
      setShowReset(true);
    }
    catch(err) {
      console.log(err);
      alert("failed to create reset token");
    }
  }

  const onConfigure = async () => {
    try {
      await setAdmin(name, password, domain);
      setParams(true);
      await getProfiles();
    }
    catch(err) {
      console.log(err);
      alert("failed to configure node");
    }
  }

  if(params == null) {
     return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Header />
        <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
        </div>
        <Footer />
      </div>
    );
  }
  if(params === false) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Header />
        <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', backgroundColor: '#f8f8f8', borderRadius: 8, padding: 16 }}>
            <div style={{ fontSize: 20, color: '#444444' }}>Server Configuration</div>
              <Form style={{ width: 400, borderRadius: 16 }}>
                <Form.Control type="text" placeholder="Name" spellCheck="false" value={name} onChange={(ev) => setName(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="text" placeholder="Domain" spellCheck="false" value={domain} onChange={(ev) => setDomain(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Password" value={password} onChange={(ev) => setPassword(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Confirm Password" value={confirm} onChange={(ev) => setConfirm(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
              </Form>
              <Button variant="primary" disabled={name==='' || password==='' || domain==='' || password !== confirm} onClick={onConfigure}>Configure</Button>
          </div>
        </div>
        <Footer />
      </div>
    );
  }  
  if(ids == null) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Header />
        <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', backgroundColor: '#f8f8f8', borderRadius: 8, padding: 16 }}>
            <div style={{ fontSize: 20, color: '#444444' }}>Admin Console</div>
              <Form style={{ width: 400, borderRadius: 16 }}>
                <Form.Control type="text" placeholder="Name" spellCheck="false" value={name} onChange={(ev) => setName(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Password" value={password} onChange={(ev) => setPassword(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
              </Form>
              <Button variant="primary" disabled={name==='' || password===''} onClick={onLogin}>Login</Button>
          </div>
        </div>
        <Footer />
      </div>
    );
  }
  else {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Header />
        <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ display: 'flex', flexDirection: 'column', backgroundColor: '#f8f8f8', borderRadius: 8, paddingTop: 8, paddingLeft: 8, paddingRight: 8, width: 800, minHeight: 300, maxHeight: '75vh' }}>
            <div style={{ display: 'flex', flexDirection: 'row', borderBottom: '1px solid #aaaaaa' }}>
              <div style={{ flexGrow: 1, paddingLeft: 16, fontSize: 20 }}>
                <span>Accounts</span><BsArrowRepeat style={{ marginLeft: 8, cursor: 'pointer', color: '#0077CC' }} onClick={getProfiles} />
              </div>
              <div style={{ paddingRight: 16, textAlign: 'right' }}>
                <FaUserPlus style={{ color: '#0077CC', cursor: 'pointer', fontSize: 24 }} onClick={onCreateAccount} />
              </div>
            </div>
            <div style={{ display: 'flex', flexGrow: 1, overflow: 'auto' }}>
              <Accounts />
            </div>
          </div>
        </div>
        <Footer />
        <CreateAccount />
      </div>
    );
  }
}

function Account() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [reset, setReset] = useState(null);
  const [create, setCreate] = useState(null);

  const [createUsername, setCreateUsername] = useState('');
  const [createPassword, setCreatePassword] = useState('');
  const [createConfirm, setCreateConfirm] = useState('');

  const [resetPassword, setResetPassword] = useState('');
  const [resetConfirm, setResetConfirm] = useState('');

  const [amigo, setAmigo] = useState(null);
  const [creating, setCreating] = useState(false);

  const [createColor, setCreateColor] = useState('#444444');
  const [show, setShow] = useState(false);
  const [update, setUpdate] = useState('');
  const [updateColor, setUpdateColor] = useState('#444444');
  const [updatable, setUpdatable] = useState(false);

  let query = useQuery();
  useEffect(() => {
    checkReset(query.get("reset"));
    checkCreate(query.get("create"));
  }, [query]);

  const checkReset = async (token) => {
    if(token != null) {
      if(await checkToken(token)) {
        setReset(token);
      }
      else {
        setReset(null);
      }
    }
  }

  const checkCreate = async (token) => {
    if(token != null) {
      if(await checkToken(token)) {
        setCreate(token);
      }
      else {
        setCreate(null);
      }
    }
  }
 
  const onLogin = async () => {
    try {
      setAmigo(await getProfile(username, password));
    }
    catch(err) {
      console.log(err);
      alert("failed to login");
    }
  }

  const onShowModal = () => {
    setUpdate('');
    setUpdateColor('#444444');
    setUpdatable(false);
    setShow(true);
  }

  const onCloseModal = () => {
    setShow(false);
  }

  const onUpdate = async (value) => {
    setUpdate(value);
    if(/^[a-zA-Z0-9_]*$/.test(value) && await checkUsername(value, amigo.amigoId)) {
      setUpdateColor('#444444');
      setUpdatable(true);
    }
    else {
      setUpdateColor('#ff4444');
      setUpdatable(false);
    }
  }

  const onEditConfirm = async () => {
    try {
      setAmigo(await updateUsername(username, password, update));
      setUsername(update);
      setShow(false);
    }
    catch(err) {
      console.log(err);
      alert("failed to update username");
    }
  }

  const onCreate = async () => {
    setCreating(true);
    try {
      setAmigo(await createAccount(createUsername, createPassword, create));
      setUsername(createUsername);
      setPassword(createPassword);
    }
    catch(err) {
      console.log(err);
      alert("failed to create account");
    }
    setCreating(false);
  }

  const onReset = async () => {
    try {
      let a = await resetAccount(resetPassword, reset);
      setAmigo(a);
      setUsername(a.handle);
      setPassword(resetPassword);
    }
    catch(err) {
      console.log(err);
      alert("failed to reset password");
    }
  }

  const onCreateUsername = async (name) => {
    setCreateUsername(name);
    if(/^[a-zA-Z0-9_]*$/.test(name) && await checkUsername(name)) {
      setCreateColor('#444444');
    }
    else {
      setCreateColor('#ff4444');
    }
  }

  const CreateLabel = () => {
    if(creating) {
     return  <Spinner animation="border" size="sm" style={{ color: '#ffffff' }} />
    }
    return <span>Create</span>
  }

  if(amigo == null) {
    if(create != null) {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
          <Header />
          <div style={{ display: 'flex', flex: 1, flexGrow: 1, flexDirection: 'column', backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', backgroundColor: '#f8f8f8', borderRadius: 8, padding: 8 }}>
              <div style={{ fontSize: 20, color: '#444444' }}>Create Account</div>
              <Form style={{ width: 400, borderRadius: 16 }}>
                <Form.Control type="text" placeholder="Username" spellCheck="false" value={createUsername} onChange={(ev) => onCreateUsername(ev.target.value)} style={{ marginTop: 8, marginBottom: 8, color: createColor }}/>
                <Form.Control type="password" placeholder="Password" value={createPassword} onChange={(ev) => setCreatePassword(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Confirm Password" value={createConfirm} onChange={(ev) => setCreateConfirm(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
              </Form>
              <Button variant="primary" disabled={createUsername==='' || createPassword==='' || createConfirm !== createPassword} onClick={onCreate} style={{ width: 92 }}>
	        <CreateLabel />
	      </Button>
            </div>
            <div style={{ fontSize: 12, color: '#888888', paddingTop: 16, fontStyle: 'italic' }}>Note: username must be unique and contain only alphanumeric and _ characters.</div>
          </div> 
          <Footer />
        </div>
      );
    }
    if(reset != null) {
      return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
          <Header />
          <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', backgroundColor: '#f8f8f8', borderRadius: 8, padding: 8 }}>
              <div style={{ fontSize: 20, color: '#444444' }}>Reset Password</div>
              <Form style={{ width: 400, borderRadius: 16 }}>
                <Form.Control type="password" placeholder="Password" value={resetPassword} onChange={(ev) => setResetPassword(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Confirm Password" value={resetConfirm} onChange={(ev) => setResetConfirm(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
              </Form>
              <Button variant="primary" disabled={resetPassword==='' || resetConfirm !== resetPassword} onClick={onReset}>Reset</Button>
            </div>
          </div> 
          <Footer />
        </div>
      );
    }
    return (
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Header />
        <div style={{ display: 'flex', flex: 1, flexGrow: 1, backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', backgroundColor: '#f8f8f8', borderRadius: 8, padding: 8 }}>
            <div style={{ fontSize: 20, color: '#444444' }}>Account Console</div>

              <Form style={{ width: 400, borderRadius: 16 }}>
                <Form.Control type="text" placeholder="Username" spellCheck="false" value={username} onChange={(ev) => setUsername(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
                <Form.Control type="password" placeholder="Password" value={password} onChange={(ev) => setPassword(ev.target.value)} style={{ marginTop: 8, marginBottom: 8 }}/>
              </Form>

              <Button variant="primary" disabled={username==='' || password===''} onClick={onLogin}>Login</Button>
          </div>
        </div>
        <Footer />
      </div>
    );
  }

  const Handle = () => {
    if(amigo.handle === null) {
      return (<span style={{ color: '#888888' }}>Unset Handle</span>);
    }
    return (<span>{ amigo.handle }</span>);
  }

  const Name = () => {
    if(amigo.name == null) {
      return (<span style={{ color: '#888888' }}>Unset Name</span>);
    }
    return (<span>{ amigo.name }</span>);
  }

  let source = avatar;
  if(amigo.logo != null) {
    source = "data:image/png;base64," + amigo.logo;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Header />
      <div style={{ display: 'flex', flex: 1, flexGrow: 1, flexDirection: 'column', backgroundColor: '#B1DAFB', alignItems: 'center', justifyContent: 'center' }}>
        <div style={{ display: 'flex', flexDirection: 'row', padding: 8, minWidth: 500, borderRadius: 8, backgroundColor: '#ffffff' }}>
          <img src={source} alt='' style={{ display: 'flex', borderRadius: 8, width: 128, height: 128 }} />
          <div style={{ display: 'flex', flexDirection: 'column', flexGrow: 1, paddingLeft: 24 }}>
            <div style={{ display: 'flex', fontSize: 18 }}>
              <Handle /><FaEdit style={{ color: '#0077cc', cursor: 'pointer', marginTop: 4, marginLeft: 8 }} onClick={onShowModal} />
            </div>
            <div style={{ display: 'flex', fontSize: 16 }}><Name /></div>
            <div style={{ display: 'flex', fontSize: 16 }}>{ amigo.location }</div>
            <div style={{ display: 'flex', fontSize: 16, flexGrow: 1 }}>{ amigo.description }</div>
            <div style={{ display: 'flex', fontSize: 12, lineBreak: 'anywhere' }}>ID: { amigo.amigoId }</div>
          </div>
        </div>
      </div>
      <Modal show={show} onHide={onCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title style={{ fontSize: 18 }}>Edit Username</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div>
              <Form.Control type="text" placeholder="Username" spellCheck="false" style={{ display: 'flex', color: updateColor, width: 300, fontSize: 18 }} value={update} onChange={(ev) => onUpdate(ev.target.value)} />
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="warning" disabled={update==='' || !updatable} onClick={onEditConfirm}>Update</Button>
          <Button variant="secondary" onClick={onCloseModal}>Cancel</Button>
        </Modal.Footer>
      </Modal>
      <Footer />
    </div>
  );

}

export default App;
