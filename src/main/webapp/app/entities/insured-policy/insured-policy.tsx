import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './insured-policy.reducer';

export const InsuredPolicy = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const insuredPolicyList = useAppSelector(state => state.insuredPolicy.entities);
  const loading = useAppSelector(state => state.insuredPolicy.loading);
  const totalItems = useAppSelector(state => state.insuredPolicy.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="insured-policy-heading" data-cy="InsuredPolicyHeading">
        Insured Policies
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/insured-policy/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Insured Policy
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {insuredPolicyList && insuredPolicyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('policyNumber')}>
                  Policy Number <FontAwesomeIcon icon={getSortIconByFieldName('policyNumber')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  Start Date <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  End Date <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('premiumAmount')}>
                  Premium Amount <FontAwesomeIcon icon={getSortIconByFieldName('premiumAmount')} />
                </th>
                <th className="hand" onClick={sort('coverageAmount')}>
                  Coverage Amount <FontAwesomeIcon icon={getSortIconByFieldName('coverageAmount')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th>
                  Policy Quotation <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Insured Farmer <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Farm <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Quotation <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {insuredPolicyList.map((insuredPolicy, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/insured-policy/${insuredPolicy.id}`} color="link" size="sm">
                      {insuredPolicy.id}
                    </Button>
                  </td>
                  <td>{insuredPolicy.policyNumber}</td>
                  <td>
                    {insuredPolicy.startDate ? <TextFormat type="date" value={insuredPolicy.startDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {insuredPolicy.endDate ? <TextFormat type="date" value={insuredPolicy.endDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{insuredPolicy.premiumAmount}</td>
                  <td>{insuredPolicy.coverageAmount}</td>
                  <td>{insuredPolicy.status}</td>
                  <td>
                    {insuredPolicy.policyQuotation ? (
                      <Link to={`/quotation/${insuredPolicy.policyQuotation.id}`}>{insuredPolicy.policyQuotation.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {insuredPolicy.insuredFarmer ? (
                      <Link to={`/farmer/${insuredPolicy.insuredFarmer.id}`}>{insuredPolicy.insuredFarmer.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{insuredPolicy.farm ? <Link to={`/farm/${insuredPolicy.farm.id}`}>{insuredPolicy.farm.id}</Link> : ''}</td>
                  <td>
                    {insuredPolicy.quotation ? (
                      <Link to={`/quotation/${insuredPolicy.quotation.id}`}>{insuredPolicy.quotation.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/insured-policy/${insuredPolicy.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/insured-policy/${insuredPolicy.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/insured-policy/${insuredPolicy.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Insured Policies found</div>
        )}
      </div>
      {totalItems ? (
        <div className={insuredPolicyList && insuredPolicyList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default InsuredPolicy;
