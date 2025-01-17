import React, {useEffect, useRef, useState} from "react";
import styled from "styled-components";
import {SearchOutlined} from "@ant-design/icons";
import CommonInput from "../../../common/basic/CommonInput";

const SearchBoxWrapper = styled.div`
    display: flex;
    justify-content: flex-start;
    margin-top: 15px;
    margin-bottom: 15px;
    margin-left: 5px;

    .ant-input-affix-wrapper {
        width: 400px;
    }
`;

const SearchBox = ({onSearch}) => {
    const [searchTerm, setSearchTerm] = useState("");
    const previousSearchTerm = useRef("");

    useEffect(() => {
        if (searchTerm !== previousSearchTerm.current) {
            const debounceTimer = setTimeout(() => {
                onSearch(searchTerm);
                previousSearchTerm.current = searchTerm;
            }, 500);

            return () => clearTimeout(debounceTimer);
        }
    }, [searchTerm, onSearch]);

    const handleReset = () => {
        setSearchTerm("");
        onSearch("");
    };

    return (
        <SearchBoxWrapper>
            <CommonInput
                placeholder="Search Anime"
                prefix={<SearchOutlined/>}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onPressEnter={() => onSearch(searchTerm)}
                allowClear
            />
        </SearchBoxWrapper>
    );
};

export default SearchBox;
