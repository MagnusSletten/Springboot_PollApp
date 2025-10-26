import { useEffect, useState } from "react";
import { BACKEND_URL } from './Constants/constants.js';
import {getCookieRaw} from './CreatePollComponent.js'

type LogInProps = {
  userName: string;
  setUsername: React.Dispatch<React.SetStateAction<string>>;
  setLoginStatus: React.Dispatch<React.SetStateAction<boolean>>;
  loginStatus: boolean;
};


export default function LogInComponent({ 
  userName,
  setUsername,
  setLoginStatus,
  loginStatus
}: LogInProps) {
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);
  const [polls, setPolls] = useState<any[]>([]);
  const [showPolls, setShowPolls] = useState(false);
  const [users, setUsers] = useState<Array<{ id:number; username:string; roles?:string[] }>>([]);

  const [showUsers, setShowUsers] = useState(false);

const fetchUsers = async () => {
  setErr(null);
  try {
    const xsrf = getCookieRaw('XSRF-TOKEN');

    const res = await fetch(`${BACKEND_URL}/users`, {
      credentials: "include", // send JSESSIONID
      headers: {
         'X-XSRF-TOKEN': xsrf,   
      }     
    });

    if (res.status === 401) throw new Error("Not logged in");
    if (res.status === 403) throw new Error("Admin only");
    if (!res.ok)  throw new Error(`Failed to load users (${res.status})`);

    const data = await res.json();
    setUsers(data);
    setShowUsers(true);
  } catch (e: any) {
    setErr(e.message ?? "Could not load users");
    setShowUsers(false);
  }
};

  const login = async () => {
    setErr(null);
    setLoading(true);
    try {
      
      const body = new URLSearchParams({
        username: userName,
        password: password,
      });

      const res = await fetch(`${BACKEND_URL}/users/auth/login`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
        },
        body: body.toString(),
      });

      if (!res.ok) throw new Error(`Login failed (${res.status})`);
      await fetch(`${BACKEND_URL}/auth/csrf`, { credentials: "include" });
      setLoginStatus(true);
    } catch (e: any) {
      setErr(e.message ?? "Login failed");
      setLoginStatus(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchMyPolls = async () => {
    try {
      const res = await fetch(`${BACKEND_URL}/polls?user=${encodeURIComponent(userName)}`, {
        credentials: "include",
      });
      if (!res.ok) throw new Error(`Failed to load polls (${res.status})`);
      const data = await res.json();
      setPolls(data);
      setShowPolls(true);
    } catch (e: any) {
      setErr(e.message ?? "Could not load polls");
    }
  };

    const deletePoll = async (id:number) => {
    try {
      const xsrf = getCookieRaw('XSRF-TOKEN');
      const res = await fetch(`${BACKEND_URL}/polls/`+id, {
        method: "DELETE",
        credentials: "include",
        headers: {
         'X-XSRF-TOKEN': xsrf,
        
      }
      });
      if (!res.ok) throw new Error(`Failed to delete polls (${res.status})`);
    } catch (e: any) {
      setErr(e.message ?? "Could not delete polls");
    }
    fetchMyPolls()
  };
return (
  <div>
    {/* login form (only if not logged in) */}
    {!loginStatus && (
      <div className="login-section">
        <input
          className="loginField"
          onChange={(e) => setUsername(e.target.value)}
          value={userName}
          placeholder="Username"
          autoComplete="username"
        />
        <input
          className="loginField"
          type="password"
          onChange={(e) => setPassword(e.target.value)}
          value={password}
          placeholder="Password"
          autoComplete="current-password"
        />
        <button className="loginButton" onClick={login} disabled={loading}>
          {loading ? "Logging in..." : "Log in"}
        </button>
      </div>
    )}
    {err && <div className="error">{err}</div>}
    {loginStatus && (
    <>
    <div style={{ marginTop: "1rem" }}>
    
      <button className="pollButton" onClick={fetchMyPolls}>
        Show My Polls
      </button>
      {showPolls && (
        <ul className="poll-list">
          {polls.map((poll) => (
            <li key={poll.id} className="poll-item">
              <span className="poll-title">{poll.title}</span>
              <button
                className="delete-button"
                onClick={() => deletePoll(poll.id)}
              >
                X
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
    <div style={{ marginTop: "1rem" }}>
  <button className="pollButton" onClick={fetchUsers}>
    Show Users (admin)
  </button>
  {showUsers && (
    <ul className="poll-list" style={{ marginTop: "0.5rem" }}>
      {users.map(u => (
        <li key={u.id} className="poll-item">
          <span className="poll-title">{u.username}</span>
          {u.roles?.length ? <small> — {u.roles.join(", ")}</small> : null}
        </li>
      ))}
    </ul>
  )}
</div>
</>
    
  )

    
    }

  </div>
);
}