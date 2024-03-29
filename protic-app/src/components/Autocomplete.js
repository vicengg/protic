import React, { useState, useEffect, useRef } from 'react';
import { useGetData } from '../hooks/useGetData'
import { useResize } from '../hooks/useResize'

export const Autocomplete = ({ url, placeholder, footer, value, onChange, onSelect, onSubmit, styleClasses = "" }) => {

    const [timeoutHandler, setTimeoutHandler] = useState(null);
    const [valueWithDelay, setValueWithDelay] = useState(value);

    useEffect(() => {
        clearTimeout(timeoutHandler);
        setTimeoutHandler(setTimeout(() => setValueWithDelay(value), 500));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [value]);

    const [suggestionsMenuVisibility, setSuggestionsMenuVisibility] = useState(false);
    const { loading, data } = useGetData(`${url}${valueWithDelay}`);
    const [suggestions, setSuggestions] = useState([]);
    const [keyboardSelection, setKeyboardSelection] = useState(null);
    const inputRef = useRef();
    const [suggestionsSize, suggestionsPosition] = useResize(inputRef);

    const clearKeyboardSelection = () => {
        setKeyboardSelection(null);
    }

    const openSuggestionsMenu = () => {
        setSuggestionsMenuVisibility(true);
    }

    const closeSuggestionsMenu = () => {
        setSuggestionsMenuVisibility(false);
    }

    const handleInputChange = (event) => {
        clearKeyboardSelection();
        openSuggestionsMenu();
        onChange(event.target.value);
    };

    const handleInputFocus = () => {
        openSuggestionsMenu();
    };

    const handleInputBlur = () => {
        closeSuggestionsMenu();
    };

    const handleSuggestionClick = (event) => {
        onSelect(event.target.innerText);
        closeSuggestionsMenu();
    };

    const handleFormSubmit = (event) => {
        event.preventDefault();
        onSubmit(!!keyboardSelection ? keyboardSelection : value);
        closeSuggestionsMenu();
    };

    const handleInputKeyDown = (event) => {
        switch (event.key) {
            case "ArrowUp":
                changeKeyboardSelecction(-1);
                break;
            case "ArrowDown":
                changeKeyboardSelecction(1);
                break;
            default:
                break;
        }
    };

    const changeKeyboardSelecction = (increasement) => {
        if (!!suggestions && suggestions.length > 0) {
            if (!!keyboardSelection) {
                const index = suggestions.indexOf(keyboardSelection);
                setKeyboardSelection(suggestions[index + increasement >= 0 ? index + increasement : suggestions.length - increasement]);
            } else {
                if (increasement === 1) {
                    setKeyboardSelection(suggestions[0]);
                } else if (increasement === -1) {
                    setKeyboardSelection(suggestions[suggestions.length - 1]);
                }
            }
        }
    }

    useEffect(() => {
        if (!loading) {
            setSuggestions(data.data);
        }
    }, [loading, data]);

    return (
        <>
            <form onSubmit={handleFormSubmit} autoComplete="off" className="component-autocomplete">
                <div className="input-container form-group">
                    <input
                        ref={inputRef}
                        className={`form-control ${styleClasses}`}
                        type="text"
                        value={value}
                        onChange={handleInputChange}
                        placeholder={placeholder}
                        onFocus={handleInputFocus}
                        onKeyDown={handleInputKeyDown}
                        onBlur={handleInputBlur}
                    />
                    <div className="suggestions" style={{ width: suggestionsSize.width, top: suggestionsPosition.top }}>
                        {!!suggestionsMenuVisibility && !!suggestions && suggestions.map(element => {
                            return <div
                                onMouseDown={handleSuggestionClick}
                                className={element === keyboardSelection ? "suggestion p-1 bg-primary" : "suggestion p-1"}
                                key={element}>{element}</div>
                        })
                        }</div>
                    {!!footer && <small className="form-text text-muted">{footer}</small>}
                </div>
            </form>
        </>
    );

};
