import React from 'react';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { CardSkeleton } from '../CardSkeleton';
import { OwnWorkExperience } from '../OwnWorkExperience';

export const MyWorkExperiencesView = () => {

    const [{ loading, data }, reload] = useGetWorkExperiences({scope: 'own'});
    const skeletonPieces = [...Array(5).keys()];

    return (
        <>
            <div className="container">
                <h1>Mis experiencias laborales</h1>
                {!!loading && skeletonPieces.map(piece =>
                    <div key={piece} className="row mb-2">
                        <div className="col-md-12">
                            <CardSkeleton lines={8} />
                        </div>
                    </div>
                )}
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
