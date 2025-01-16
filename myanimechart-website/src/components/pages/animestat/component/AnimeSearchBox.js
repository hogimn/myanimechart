import React, {useState} from "react";
import styled from "styled-components";
import {CloseOutlined, SearchOutlined} from "@ant-design/icons";
import CommonInput from "../../../common/basic/CommonInput";
import CommonButton from "../../../common/basic/CommonButton";

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

const ButtonWrapper = styled.div`
    display: flex;
    align-items: center;
    margin-left: 10px;

    .ant-btn + .ant-btn {
        margin-left: 5px
    }
`;

const SearchBox = ({onSearch}) => {
    const [searchTerm, setSearchTerm] = useState("");

    const handleSearch = (value) => {
        onSearch(value);
    };

    const handleReset = () => {
        setSearchTerm("");
        onSearch("");
    };

    return (
        <SearchBoxWrapper>
            <CommonInput
                placeholder="Search Anime"
                prefix={<SearchOutlined />}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onPressEnter={() => handleSearch(searchTerm)}
                allowClear
            />
            <ButtonWrapper>
                <CommonButton onClick={handleReset}>
                    <CloseOutlined/>
                </CommonButton>
            </ButtonWrapper>
        </SearchBoxWrapper>
    );
};

export default SearchBox;
