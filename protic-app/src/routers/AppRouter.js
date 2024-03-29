import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import { AddWorkExperienceView } from '../components/views/AddWorkExperienceView'
import { MyWorkExperiencesView } from '../components/views/MyWorkExperiencesView'
import { ModifyWorkExperienceView } from "../components/views/ModifyWorkExperienceView";
import { SearchWorkExperiencesView } from "../components/views/SearchWorkExperiencesView";
import { CreateNegotiationView } from "../components/views/CreateNegotiationView";
import { MyNegotiationsView } from "../components/views/MyNegotiationsView";
import { NegotiationDetailsView } from "../components/views/NegotiationDetailsView";
import { NavbarUser } from "../components/NavbarUser";

export default function AppRouter() {

  const [collapse, setCollapse] = useState(true);

  const toggleCollapse = () => {
    setCollapse(!collapse);
  }

  return (
    <Router>
      <div>
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-5">
            <span className="navbar-brand">ProTIC</span>

          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation" onClick={toggleCollapse}>
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className={`${collapse ? "collapse" : ""} navbar-collapse`} id="navbarSupportedContent">
            <ul className="navbar-nav mr-auto">
              <li className="nav-item active p-2">
                <Link className="text-light" to="/add-work-experience">Añadir experiencia</Link>
              </li>
              <li className="nav-item p-2">
                <Link className="text-light" to="/my-work-experiences">Mis experiencias</Link>
              </li>
              <li className="nav-item p-2">
                <Link className="text-light" to="/search-work-experiences">Buscar experiencias</Link>
              </li>
              <li className="nav-item p-2">
                <Link className="text-light" to="/my-negotiations">Mis solicitudes</Link>
              </li>
            </ul>
          </div>

          <NavbarUser />
        </nav>

        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
        <Switch>
          <Route path="/add-work-experience">
            <AddWorkExperienceView />
          </Route>
          <Route path="/my-work-experiences">
            <MyWorkExperiencesView />
          </Route>
          <Route path="/search-work-experiences">
            <SearchWorkExperiencesView />
          </Route>
          <Route path="/modify-work-experience/:workExperienceId">
            <ModifyWorkExperienceView />
          </Route>
          <Route path="/create-information-request/:demandedWorkExperienceId">
            <CreateNegotiationView />
          </Route>
          <Route path="/my-negotiations">
            <MyNegotiationsView />
          </Route>
          <Route path="/negotiation-details/:negotiationId">
            <NegotiationDetailsView />
          </Route>
          <Route path="/">
            <MyWorkExperiencesView />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}
