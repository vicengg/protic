import {useState} from 'react';
import {nullIfEmpty} from '../helpers/nullHelpers';

export const useWorkExperience = () =>  {

    const [workExperience, setWorkExperience] = useState({
        jobTitle: {
            public: false,
            content: null
        },
        company: {
            public: false,
            content: null
        },
        technologies: {
            public: false,
            content: []
        },
        salary: {
            public: false,
            content: {
                value: 0,
                currency: "EUR"
            }
        },
        workPeriod: {
            public: false,
            content: {
                startDate: null,
                endDate: null
            }
        },
        binding: false
    });

    const changeField = (field) => {
        return (value) => {
            setWorkExperience({ ...workExperience, [field]: { ...workExperience[field], content: nullIfEmpty(value) } });
        }
    }

    const toggleVisibility = (field) => {
        return () => {
            setWorkExperience({ ...workExperience, [field]: { ...workExperience[field], public: !workExperience[field].public } });
        }
    }

    const changeWorkPeriodDate = (field) => {
        return (value) => {
            setWorkExperience({
                ...workExperience,
                workPeriod: {
                    ...workExperience.workPeriod,
                    content: {
                        ...workExperience.workPeriod.content,
                        [field]: nullIfEmpty(value)
                    }
                }
            });
        }
    }

    const toggleBinding = () => {
        setWorkExperience({ ...workExperience, binding: !workExperience.binding});
    }

    return [workExperience, changeField, toggleVisibility, changeWorkPeriodDate, toggleBinding];
};