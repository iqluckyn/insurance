import farmer from 'app/entities/farmer/farmer.reducer';
import business from 'app/entities/business/business.reducer';
import businessType from 'app/entities/business-type/business-type.reducer';
import insuredPolicy from 'app/entities/insured-policy/insured-policy.reducer';
import policyClaim from 'app/entities/policy-claim/policy-claim.reducer';
import pComponent from 'app/entities/p-component/p-component.reducer';
import policyComponent from 'app/entities/policy-component/policy-component.reducer';
import farm from 'app/entities/farm/farm.reducer';
import product from 'app/entities/product/product.reducer';
import cropType from 'app/entities/crop-type/crop-type.reducer';
import season from 'app/entities/season/season.reducer';
import quotation from 'app/entities/quotation/quotation.reducer';
import quotationStatus from 'app/entities/quotation-status/quotation-status.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  farmer,
  business,
  businessType,
  insuredPolicy,
  policyClaim,
  pComponent,
  policyComponent,
  farm,
  product,
  cropType,
  season,
  quotation,
  quotationStatus,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
