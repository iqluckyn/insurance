import React from 'react';
import { UncontrolledDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const AccountMenuItemsAuthenticated = () => (
  <>
    <DropdownItem tag={Link} to="/account/settings" data-cy="settings">
      <FontAwesomeIcon icon="wrench" fixedWidth /> Settings
    </DropdownItem>
    <DropdownItem tag={Link} to="/account/password" data-cy="passwordItem">
      <FontAwesomeIcon icon="lock" fixedWidth /> Password
    </DropdownItem>
    <DropdownItem divider />
    <DropdownItem tag={Link} to="/logout" data-cy="logout">
      <FontAwesomeIcon icon="sign-out-alt" fixedWidth /> Sign out
    </DropdownItem>
  </>
);

const AccountMenuItems = () => (
  <>
    <DropdownItem tag={Link} to="/login" data-cy="login">
      <FontAwesomeIcon icon="sign-in-alt" fixedWidth /> Sign in
    </DropdownItem>
    <DropdownItem tag={Link} to="/account/register" data-cy="register">
      <FontAwesomeIcon icon="user-plus" fixedWidth /> Register
    </DropdownItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => (
  <UncontrolledDropdown nav inNavbar data-cy="accountMenu">
    <DropdownToggle nav caret className="d-flex align-items-center">
      <FontAwesomeIcon icon="user" fixedWidth />
      <span className="d-none d-md-inline">Account</span>
    </DropdownToggle>
    <DropdownMenu end>{isAuthenticated ? <AccountMenuItemsAuthenticated /> : <AccountMenuItems />}</DropdownMenu>
  </UncontrolledDropdown>
);

export default AccountMenu;
