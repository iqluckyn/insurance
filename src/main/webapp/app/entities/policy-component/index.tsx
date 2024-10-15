import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PolicyComponent from './policy-component';
import PolicyComponentDetail from './policy-component-detail';
import PolicyComponentUpdate from './policy-component-update';
import PolicyComponentDeleteDialog from './policy-component-delete-dialog';

const PolicyComponentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PolicyComponent />} />
    <Route path="new" element={<PolicyComponentUpdate />} />
    <Route path=":id">
      <Route index element={<PolicyComponentDetail />} />
      <Route path="edit" element={<PolicyComponentUpdate />} />
      <Route path="delete" element={<PolicyComponentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PolicyComponentRoutes;
