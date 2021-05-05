import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import { HomeView } from '../components/views/HomeView'
import { AddWorkExperienceView } from '../components/views/AddWorkExperienceView'
import { MyWorkExperiencesView } from '../components/views/MyWorkExperiencesView'
import { ModifyWorkExperienceView } from "../components/views/ModifyWorkExperienceView";
import { SearchWorkExperiencesView } from "../components/views/SearchWorkExperiencesView";

export default function AppRouter() {
  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li>
              <Link to="/">Home</Link>
            </li>
            <li>
              <Link to="/add-work-experience">Add work experience</Link>
            </li>
            <li>
              <Link to="/my-work-experiences">My work experiences</Link>
            </li>
            <li>
              <Link to="/search-work-experiences">Search work experiences</Link>
            </li>
          </ul>
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
          <Route path="/">
            <HomeView />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}
