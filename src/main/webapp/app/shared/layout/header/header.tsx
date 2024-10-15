import './header.scss';
import React, { useState } from 'react';
import { Navbar, Nav, NavbarToggler, Collapse, NavItem } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';

import { AdminMenu, EntitiesMenu, AccountMenu, SetupMenu } from '../menus';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}

const Home = () => (
  <NavItem>
    <Link to="/" className="nav-link">
      <FontAwesomeIcon icon={faHome} />
      <span>Home</span>
    </Link>
  </NavItem>
);

const Brand = () => (
  <Link to="/" className="navbar-brand">
    <span>Policy Administration</span>
  </Link>
);

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const renderDevRibbon = () =>
    props.ribbonEnv === 'dev' ? (
      <div className="ribbon dev">
        <a href="">Development</a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" dark expand="md" fixed="top" className="bg-dark">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ms-auto" navbar>
            <Home />
            {props.isAuthenticated && (
              <>
                <EntitiesMenu />
                {props.isAdmin && <SetupMenu />}
              </>
            )}
            {props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} showDatabase={!props.isInProduction} />}
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
