import React, { useState } from 'react';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { WorkExperience } from '../WorkExperience';
import { emptyIfNull, nullIfEmpty } from '../../helpers/nullHelpers';
import { Autocomplete } from '../Autocomplete';
import { AutocompleteMultiple } from '../AutocompleteMultiple';
import { DateInput } from '../DateInput';
import { MoneyInput } from '../MoneyInput';
import { CardSkeleton } from '../CardSkeleton';

export const SearchWorkExperiencesView = () => {

    const skeletonPieces = [...Array(5).keys()];

    const [searchFilters, setSearchFilters] = useState({
        scope: "foreign",
        jobTitle: null,
        company: null,
        technologies: [],
        startDate: null,
        endDate: null,
        minSalary: null,
        maxSalary: null
    }, 400);

    const [{ loading, data }] = useGetWorkExperiences(searchFilters);
    const [{ data: ownWorkExperiencesData }] = useGetWorkExperiences({ scope: "own" });

    const changeField = (field) => {
        return (value) => setSearchFilters({ ...searchFilters, [field]: nullIfEmpty(value) });
    }

    const changeSalary = (field) => {
        return (value) => setSearchFilters({ ...searchFilters, [field]: nullIfEmpty(value.value) });
    }

    return (
        <>
            <div className="container">
                <h1>Búsqueda experiencias laborales</h1>
                <div className="row">
                    <div className="col-md-8">
                        <div className="row">
                            <div className="col-md-6">
                                <Autocomplete
                                    styleClasses="form-control-sm"
                                    url="/data/job-titles?name="
                                    placeholder="Profesión"
                                    footer="Filtrar por profesión"
                                    value={emptyIfNull(searchFilters.jobTitle)}
                                    onChange={changeField('jobTitle')}
                                    onSelect={changeField('jobTitle')}
                                    onSubmit={changeField('jobTitle')} />
                            </div>
                            <div className="col-md-6">
                                <Autocomplete
                                    styleClasses="form-control-sm"
                                    url="/data/companies?name="
                                    placeholder="Empresa"
                                    footer="Filtrar por empresa"
                                    value={emptyIfNull(searchFilters.company)}
                                    onChange={changeField('company')}
                                    onSelect={changeField('company')}
                                    onSubmit={changeField('company')} />
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-6">
                                <MoneyInput
                                    styleClasses="input-group-sm"
                                    placeholder="Salario desde..."
                                    footer="Introduzca un salario mínimo"
                                    value={{ value: searchFilters.minSalary, currency: 'EUR' }}
                                    onChange={changeSalary('minSalary')} />
                            </div>
                            <div className="col-md-6">
                                <MoneyInput
                                    styleClasses="input-group-sm"
                                    footer="Introduzca un salario máximo"
                                    placeholder="Salario hasta..."
                                    value={{ value: searchFilters.maxSalary, currency: 'EUR' }}
                                    onChange={changeSalary('maxSalary')} />
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-md-6">
                                <DateInput
                                    styleClasses="form-control-sm"
                                    footer="Introduzca una fecha de entrada mínima"
                                    value={emptyIfNull(searchFilters.startDate)}
                                    onChange={changeField("startDate")} />
                            </div>
                            <div className="col-md-6">
                                <DateInput
                                    styleClasses="form-control-sm"
                                    footer="Introduzca una fecha de entrada hasta máxima"
                                    value={emptyIfNull(searchFilters.endDate)}
                                    onChange={changeField("endDate")} />
                            </div>
                        </div>

                    </div>
                    <div className="col-md-4">
                        <AutocompleteMultiple
                            styleClasses="form-control-sm"
                            url="/data/technologies?name="
                            placeholder="Tecnologías"
                            footer="Filtrar por tecnologías"
                            values={searchFilters.technologies}
                            setValues={changeField('technologies')} />
                    </div>
                </div>

                {!!loading && skeletonPieces.map(piece =>
                    <div className="row mb-2" key={piece}>
                        <div className="col-md-12">
                            <CardSkeleton lines={6} />
                        </div>
                    </div>
                )}

                {!loading && !!data && data.result.map(workExperience =>
                    <div className="row mb-2" key={workExperience.id}>
                        <div className="col-md-12">
                            <WorkExperience workExperience={workExperience} enableNegotiation={!!ownWorkExperiencesData && ownWorkExperiencesData.result.length > 0} />
                        </div>
                    </div>
                )}
            </div>
        </>
    );

};
