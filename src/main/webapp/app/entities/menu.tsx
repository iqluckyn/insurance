import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faListAlt,
  faUser,
  faBuilding,
  faFileContract,
  faFileInvoice,
  faShieldAlt,
  faHandHoldingUsd,
} from '@fortawesome/free-solid-svg-icons';

const EntitiesMenu = () => {
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggle = () => setDropdownOpen(prevState => !prevState);

  return (
    <Dropdown nav isOpen={dropdownOpen} toggle={toggle}>
      <DropdownToggle nav caret>
        <FontAwesomeIcon icon={faListAlt} fixedWidth /> Processing
      </DropdownToggle>
      <DropdownMenu>
        {/* Farmer Information Group */}
        <DropdownItem header>Farmer Information</DropdownItem>
        <DropdownItem tag={Link} to="/farmer">
          <FontAwesomeIcon icon={faUser} fixedWidth /> Farmer
        </DropdownItem>
        <DropdownItem tag={Link} to="/view-farmer-insured-policy">
          <FontAwesomeIcon icon={faFileContract} fixedWidth /> Active Policies
        </DropdownItem>
        <DropdownItem divider />

        {/* Processing Group */}
        <DropdownItem header>Processing</DropdownItem>
        <DropdownItem tag={Link} to="/quotation">
          <FontAwesomeIcon icon={faFileInvoice} fixedWidth /> Quotations
        </DropdownItem>
        <DropdownItem tag={Link} to="/insured-policy">
          <FontAwesomeIcon icon={faShieldAlt} fixedWidth /> Insured Policy
        </DropdownItem>
        <DropdownItem tag={Link} to="/policy-claim">
          <FontAwesomeIcon icon={faHandHoldingUsd} fixedWidth /> Claims Processing
        </DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );
};

export default EntitiesMenu;
