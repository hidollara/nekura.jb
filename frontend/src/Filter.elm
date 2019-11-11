module Filter exposing
  ( Model
  , fromQueryString
  , toQueryParameters
  )

import Url.Builder
import Url.Parser.Query



type alias Model =
  { musicTitles : List String
  , diffs : FilterDiffs
  , modes : FilterModes
  , playerNames : List String
  , rivalIds : List Int
  }

type alias FilterDiffs =
  { basic : Bool
  , advanced : Bool
  , extreme : Bool
  }

type alias FilterModes =
  { normal : Bool
  , hard : Bool
  }


fromQueryString : Url.Parser.Query.Parser Model
fromQueryString =
  Url.Parser.Query.map Model (Url.Parser.Query.custom "musicTitles" identity)
    |> apply (Url.Parser.Query.custom "diffs" listToFilterDiffs)
    |> apply (Url.Parser.Query.custom "modes" listToFilterModes)
    |> apply (Url.Parser.Query.custom "playerNames" identity)
    |> apply (Url.Parser.Query.custom "rivalIds" (List.filterMap String.toInt))


apply : Url.Parser.Query.Parser a -> Url.Parser.Query.Parser (a -> b) -> Url.Parser.Query.Parser b
apply argParser funcParser =
  Url.Parser.Query.map2 (<|) funcParser argParser


listToFilterDiffs : List String -> FilterDiffs
listToFilterDiffs list =
  { basic = List.member "BASIC" list
  , advanced = List.member "ADVANCED" list
  , extreme = List.member "EXTREME" list
  }


listToFilterModes : List String -> FilterModes
listToFilterModes list =
  { normal = List.member "NORMAL" list
  , hard = List.member "HARD" list
  }


toQueryParameters : Model -> List Url.Builder.QueryParameter
toQueryParameters filter =
  List.concat
    [ List.map (Url.Builder.string "musicTitles") filter.musicTitles
    , List.filterMap identity
        [ if filter.diffs.basic then Just (Url.Builder.string "diffs" "BASIC") else Nothing
        , if filter.diffs.advanced then Just (Url.Builder.string "diffs" "ADVANCED") else Nothing
        , if filter.diffs.extreme then Just (Url.Builder.string "diffs" "EXTREME") else Nothing
        ]
    , List.filterMap identity
        [ if filter.modes.normal then Just (Url.Builder.string "modes" "NORMAL") else Nothing
        , if filter.modes.hard then Just (Url.Builder.string "modes" "HARD") else Nothing
        ]
    , List.map (Url.Builder.string "playerNames") filter.playerNames
    , List.map (Url.Builder.int "rivalIds") filter.rivalIds
    ]
