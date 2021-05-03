import React from 'react';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { WorkExperience } from '../WorkExperience';

export const MyWorkExperiencesView = () => {

    const [{ loading, data }, reload] = useGetWorkExperiences('own');

    return (
        <>
            <div className="container">
                <h1>Mis experiencias laborales</h1>
                {!loading && !!data && data.result.map(workExperience =>
                    <div className="row mb-5" key={workExperience.id}>
                        <div className="col-md-12">
                            <WorkExperience workExperience={workExperience} afterDelete={reload} />
                        </div>
                    </div>
                )}
            </div>
        </>
    );

};
