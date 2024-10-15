import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PolicyClaim from './policy-claim';
import PolicyClaimDetail from './policy-claim-detail';
import PolicyClaimUpdate from './policy-claim-update';
import PolicyClaimDeleteDialog from './policy-claim-delete-dialog';

const PolicyClaimRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PolicyClaim />} />
    <Route path="new" element={<PolicyClaimUpdate />} />
    <Route path=":id">
      <Route index element={<PolicyClaimDetail />} />
      <Route path="edit" element={<PolicyClaimUpdate />} />
      <Route path="delete" element={<PolicyClaimDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PolicyClaimRoutes;
