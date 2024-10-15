import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InsuredPolicy from './insured-policy';
import InsuredPolicyDetail from './insured-policy-detail';
import InsuredPolicyUpdate from './insured-policy-update';
import InsuredPolicyDeleteDialog from './insured-policy-delete-dialog';

const InsuredPolicyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InsuredPolicy />} />
    <Route path="new" element={<InsuredPolicyUpdate />} />
    <Route path=":id">
      <Route index element={<InsuredPolicyDetail />} />
      <Route path="edit" element={<InsuredPolicyUpdate />} />
      <Route path="delete" element={<InsuredPolicyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InsuredPolicyRoutes;
