import React, { useState, useEffect } from 'react';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { WorkExperience } from '../WorkExperience';
import { emptyIfNull } from '../../helpers/nullHelpers';
import { Autocomplete } from '../Autocomplete';
import { useStateDelay } from '../../hooks/useStateDelay';

export const SearchWorkExperiencesView = () => {

    const [searchFilters, searchFiltersDelay, setSearchFilters] = useStateDelay({jobTitle: null}, 400);
    
    const [{ loading, data }, reload] = useGetWorkExperiences('foreign', searchFiltersDelay.jobTitle);
    
    const changeField = (field) => {
        return (value) => setSearchFilters({...searchFilters, [field]: value});
    }

    return (
        <>
            <div className="container">
                <h1>Búsqueda experiencias laborales</h1>
                <Autocomplete
                            url="/data/job-titles?name="
                            placeholder="Introduce una profesión"
                            value={emptyIfNull(searchFilters.jobTitle)}
                            onChange={changeField('jobTitle')}
                            onSelect={changeField('jobTitle')}
                            onSubmit={changeField('jobTitle')} />
                {!loading && !!data && data.result.map(workExperience =>
                    <div className="row mb-2" key={workExperience.id}>
                        <div className="col-md-12">
                            <WorkExperience workExperience={workExperience} afterDelete={reload} />
                        </div>
                    </div>
                )}
            </div>
        </>
    );

};
