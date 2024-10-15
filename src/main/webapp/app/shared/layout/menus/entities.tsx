import React from 'react';
import { Nav, NavItem } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import EntitiesMenuItems from 'app/entities/menu';

export const EntitiesMenu = () => (
  <Nav vertical>
    <NavItem>
      <Nav className="ml-3">
        <EntitiesMenuItems />
      </Nav>
    </NavItem>
  </Nav>
);
