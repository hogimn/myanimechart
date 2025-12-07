import { useEffect, useRef, useState } from "react";
import { SearchOutlined } from "@ant-design/icons";
import CommonInput from "../../../common/basic/CommonInput";
import { SearchBoxWrapper } from "./AnimeSearchBox.style";

const SearchBox = ({ onSearch }) => {
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

  return (
    <SearchBoxWrapper>
      <CommonInput
        placeholder="Search Anime"
        prefix={<SearchOutlined />}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        onPressEnter={() => onSearch(searchTerm)}
        allowClear
      />
    </SearchBoxWrapper>
  );
};

export default SearchBox;
