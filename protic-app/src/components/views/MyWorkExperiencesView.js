import React from 'react';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { OwnWorkExperience } from '../OwnWorkExperience';

export const MyWorkExperiencesView = () => {

    const [{ loading, data }, reload] = useGetWorkExperiences('own');

    return (
        <>
            <div className="container">
                <h1>Mis experiencias laborales</h1>
                {!loading && !!data && data.result.map(workExperience =>
                    <div className="row mb-2" key={workExperience.id}>
                        <div className="col-md-12">
                            <OwnWorkExperience workExperience={workExperience} afterDelete={reload} editable={true}/>
                        </div>
                    </div>
                )}
            </div>
        </>
    );

};
