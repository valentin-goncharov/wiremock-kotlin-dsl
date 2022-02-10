package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.matching.EqualToXmlPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.xmlunit.diff.ComparisonType.ELEMENT_TAG_NAME
import java.util.function.Consumer

internal class XmlBodyPatternTest {

    private val scope = RequestBodyScope()

    @Test
    fun `placeholders should apply pattern placeholders`() {

        val xmlBody = """
            <xml>
                <key>value</key>
            </xml>
        """

        val pattern = XmlBodyPattern(scope)

        pattern.equalToXml(xmlBody)

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
            })

        pattern placeholders {
            enabled = true
            openingDelimiterRegex = "\\[\\["
            closingDelimiterRegex = "]]"
        }

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
                assertThat(xmlPattern.isEnablePlaceholders).isTrue
                assertThat(xmlPattern.placeholderOpeningDelimiterRegex).isEqualTo("\\[\\[")
                assertThat(xmlPattern.placeholderClosingDelimiterRegex).isEqualTo("]]")
                assertThat(xmlPattern.exemptedComparisons).isNull()
            })
    }

    @Test
    fun `exemptComparison should apply comparison`() {

        val xmlBody = """
            <xml>
                <key>value</key>
            </xml>
        """

        val pattern = XmlBodyPattern(scope)

        pattern.equalToXml(xmlBody)
        pattern exemptComparison "ELEMENT_TAG_NAME"

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
                assertThat(xmlPattern.exemptedComparisons).hasSize(1)
                assertThat(xmlPattern.exemptedComparisons).contains(ELEMENT_TAG_NAME)
            })
    }

    @Test
    fun `placeholders after exemptComparison should apply pattern placeholders and keep comparison`() {
        val xmlBody = """
            <xml>
                <key>value</key>
            </xml>
        """

        val pattern = XmlBodyPattern(scope)

        pattern.equalToXml(xmlBody)
        pattern exemptComparison "ELEMENT_TAG_NAME"
        pattern placeholders {
            enabled = true
        }

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
                assertThat(xmlPattern.isEnablePlaceholders).isTrue
                assertThat(xmlPattern.exemptedComparisons).hasSize(1)
                assertThat(xmlPattern.exemptedComparisons).contains(ELEMENT_TAG_NAME)
            })
    }
}