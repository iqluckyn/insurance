import React from 'react';
import { NavDropdown } from 'react-bootstrap';
import { NavLink } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCog,
  faBuilding,
  faClipboardCheck,
  faPuzzlePiece,
  faCalendarAlt,
  faSeedling,
  faFileAlt,
} from '@fortawesome/free-solid-svg-icons';

export const SetupMenu = () => (
  <NavDropdown
    title={
      <>
        <FontAwesomeIcon icon={faCog} />
        <span>Setups</span>
      </>
    }
    id="setup-menu"
    data-cy="setupMenu"
    style={{ marginLeft: '20px' }}
  >
    <NavDropdown.Item as={NavLink} to="/product">
      <FontAwesomeIcon icon={faCog} fixedWidth /> Products
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/business-type">
      <FontAwesomeIcon icon={faBuilding} fixedWidth /> Business Types
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/p-component">
      <FontAwesomeIcon icon={faSeedling} fixedWidth /> Policy Components
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/policy-component">
      <FontAwesomeIcon icon={faPuzzlePiece} fixedWidth /> Policy Components Values
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/season">
      <FontAwesomeIcon icon={faCalendarAlt} fixedWidth /> Seasons
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/crop-type">
      <FontAwesomeIcon icon={faSeedling} fixedWidth /> Crop Types
    </NavDropdown.Item>
    <NavDropdown.Item as={NavLink} to="/quotation-status">
      <FontAwesomeIcon icon={faFileAlt} fixedWidth /> Quotation Status
    </NavDropdown.Item>
  </NavDropdown>
);

export default SetupMenu;
