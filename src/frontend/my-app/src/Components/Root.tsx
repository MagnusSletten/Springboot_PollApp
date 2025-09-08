import { Link, Outlet } from "react-router-dom";

export default function Root() {
  return (
    <div className="main">
      <nav>
        <Link to="/">Home</Link>{" | "}
        <Link to="/new-poll">Create Poll</Link>
      </nav>
      <Outlet />
    </div>
  );
}